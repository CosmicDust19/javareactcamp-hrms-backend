package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.CvService;
import com.finalproject.hrmsbackend.core.business.abstracts.CheckService;
import com.finalproject.hrmsbackend.core.utilities.Msg;
import com.finalproject.hrmsbackend.core.utilities.Utils;
import com.finalproject.hrmsbackend.core.utilities.results.*;
import com.finalproject.hrmsbackend.dataAccess.abstracts.*;
import com.finalproject.hrmsbackend.entities.abstracts.CvProp;
import com.finalproject.hrmsbackend.entities.concretes.*;
import com.finalproject.hrmsbackend.entities.concretes.dtos.CvAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CvManager implements CvService {

    private final CandidateDao candidateDao;
    private final CvDao cvDao;
    private final CandidateJobExperienceDao candidateJobExpDao;
    private final CandidateLanguageDao candidateLangDao;
    private final CandidateSchoolDao candidateSchoolDao;
    private final CandidateSkillDao candidateSkillDao;
    private final ImageDao imageDao;
    private final ModelMapper modelMapper;
    private final CheckService check;

    @Override
    public boolean existsCandidatePropInCv(Class<?> propType, int propId, int cvId) {
        if (propId <= 0) return false;
        else if (CandidateJobExperience.class.equals(propType) && cvDao.existsCandidateJobExpInCv(propId, cvId))
            return true;
        else if (CandidateLanguage.class.equals(propType) && cvDao.existsCandidateLangInCv(propId, cvId)) return true;
        else if (CandidateSchool.class.equals(propType) && cvDao.existsCandidateSchoolInCv(propId, cvId)) return true;
        else return CandidateSkill.class.equals(propType) && cvDao.existsCandidateSkillInCv(propId, cvId);
    }

    @Override
    public DataResult<List<Cv>> getAll() {
        return new SuccessDataResult<>(cvDao.findAll());
    }

    @Override
    public DataResult<Cv> getById(int id) {
        return new SuccessDataResult<>(cvDao.getById(id));
    }

    @Override
    public Result add(CvAddDto cvAddDto) {
        if (check.notExistsById(candidateDao, cvAddDto.getCandidateId()))
            return new ErrorResult(Msg.NOT_EXIST.get("candidateId"));
        if (cvDao.existsByTitleAndCandidate(cvAddDto.getTitle(), new Candidate(cvAddDto.getCandidateId())))
            return new ErrorResult(Msg.USED.get("Title"));

        Cv cv = modelMapper.map(cvAddDto, Cv.class);
        Cv savedCv = cvDao.save(cv);

        Map<String, Result> results = new LinkedHashMap<>();
        results.put("candidateJobExperiences", addPropsToCv(savedCv.getId(), cvAddDto.getCandidateJobExperienceIds(), candidateJobExpDao, Utils.CheckType.PARTLY, CandidateJobExperience.class));
        results.put("candidateLanguages", addPropsToCv(savedCv.getId(), cvAddDto.getCandidateLanguageIds(), candidateLangDao, Utils.CheckType.PARTLY, CandidateLanguage.class));
        results.put("candidateSchools", addPropsToCv(savedCv.getId(), cvAddDto.getCandidateSchoolIds(), candidateSchoolDao, Utils.CheckType.PARTLY, CandidateSchool.class));
        results.put("candidateSkills", addPropsToCv(savedCv.getId(), cvAddDto.getCandidateSkillIds(), candidateSkillDao, Utils.CheckType.PARTLY, CandidateSkill.class));
        results.put("CV", new SuccessDataResult<>(Msg.SAVED.getCustom("%s"), cvDao.getById(savedCv.getId())));
        return new SuccessDataResult<>(Msg.SUCCESS.get(), results);
    }

    @Override
    public Result deleteById(int cvId) {
        cvDao.deleteById(cvId);
        return new SuccessResult(Msg.DELETED.get());
    }

    @Override
    public Result updateTitle(String title, int cvId) {
        if (check.notExistsById(cvDao, cvId))
            return new ErrorResult(Msg.NOT_EXIST.get("cvId"));
        if (cvDao.existsByTitleAndCandidate(title, cvDao.getById(cvId).getCandidate()))
            return new ErrorResult(Msg.USED.get("Title"));

        Cv cv = cvDao.getById(cvId);
        if (cv.getTitle().equals(title))
            return new ErrorResult(Msg.IS_THE_SAME.get("Title"));

        cv.setTitle(title);
        return execLastUpdAct(cv);
    }

    @Override
    public Result updateCoverLetter(String coverLetter, int cvId) {
        if (check.notExistsById(cvDao, cvId)) return new ErrorResult(Msg.NOT_EXIST.get("cvId"));

        Cv cv = cvDao.getById(cvId);
        if (check.equals(cv.getCoverLetter(), coverLetter))
            return new ErrorResult(Msg.IS_THE_SAME.get("Cover letter"));

        cv.setCoverLetter(coverLetter);
        return execLastUpdAct(cv);
    }

    @Override
    public Result updateImg(Integer imgId, int cvId) {
        if (check.notExistsById(cvDao, cvId)) return new ErrorResult(Msg.NOT_EXIST.get("cvId"));

        Cv cv = cvDao.getById(cvId);
        if ((cv.getImage() == null && imgId == null) || (cv.getImage() != null && check.equals(cv.getImage().getId(), imgId)))
            return new ErrorResult(Msg.IS_THE_SAME.get("Profile photo"));
        for (Image img : cv.getCandidate().getImages())
            if (imgId == null || check.equals(img.getId(), imgId)) {
                cv.setImage(imgId == null ? null : imageDao.getById(imgId));
                return execLastUpdAct(cv);
            }
        return new ErrorResult(Msg.NOT_HAVE.get("Candidate"));
    }

    @Override
    public Result addPropsToCv(int cvId, Set<Integer> cvPropIds, JpaRepository<?, Integer> cvPropDao, String checkType, Class<?> propType) {
        if (cvPropIds == null || cvPropIds.size() == 0)
            return new ErrorResult(Msg.NO_ID_FOUND.get());
        if (checkType.equals(Utils.CheckType.ALL) && check.notExistsById(cvDao, cvId))
            return new ErrorResult(Msg.NOT_EXIST.get("cvId"));

        Cv cv = cvDao.getById(cvId);
        Map<String, Object> errors = new LinkedHashMap<>();

        short counter = -1;
        for (Integer propId : cvPropIds) {
            counter++;
            //exists prop
            if (check.notExistsById(cvPropDao, propId)) {
                errors.put(String.format("%sIds[%d](id: %d)", propType.getSimpleName(), counter, propId), Msg.NOT_EXIST.get());
                continue;
            }
            //exists prop in cv
            if (checkType.equals(Utils.CheckType.ALL) && existsCandidatePropInCv(propType, propId, cvId)) {
                errors.put(String.format("%sIds[%d](id: %d)", propType.getSimpleName(), counter, propId), Msg.ALREADY_CONTAINS.get("CV"));
                continue;
            }
            //candidate have this prop
            CvProp cvProp = (CvProp) cvPropDao.getById(propId);
            if (!cv.getCandidate().getId().equals(cvProp.getCandidate().getId())) {
                errors.put(String.format("%sIds[%d] - cvId", propType.getSimpleName(), counter), Msg.NOT_HAVE.get("Candidate"));
                continue;
            }
            addPropToCv(propType, propId, cvId);
        }
        // get results
        int success = cvPropIds.size() - errors.size(), fail = cvPropIds.size() - success;
        if (success > 0) cvDao.updateLastModifiedAt(LocalDateTime.now(), cvId);
        return getPropUpdateResults(success, fail, errors, cvId);
    }

    @Override
    public Result removePropsFromCv(int cvId, Set<Integer> cvPropIds, String checkType, Class<?> propType) {
        if (cvPropIds == null || cvPropIds.size() == 0)
            return new ErrorResult(Msg.NO_ID_FOUND.get());

        // check ?
        if (checkType.equals(Utils.CheckType.ALL)) {
            if (check.notExistsById(cvDao, cvId))
                return new ErrorResult(Msg.NOT_EXIST.get("cvId"));

            Map<String, Object> errors = new LinkedHashMap<>();
            short counter = -1;
            for (Integer propId : cvPropIds) {
                counter++;
                // exists prop in CV
                if (!existsCandidatePropInCv(propType, propId, cvId)) {
                    errors.put(String.format("%sIds[%d](id: %d)", propType.getSimpleName(), counter, propId), Msg.NOT_EXIST.getCustom("%s in CV"));
                    continue;
                }
                removePropFromCv(propType, propId, cvId);
            }

            //get checked deletion results
            int success = cvPropIds.size() - errors.size(), fail = cvPropIds.size() - success;
            if (success > 0) cvDao.updateLastModifiedAt(LocalDateTime.now(), cvId);
            return getPropUpdateResults(success, fail, errors, cvId);
        }

        //delete simply
        cvPropIds.forEach((propId) -> removePropFromCv(propType, propId, cvId));
        cvDao.updateLastModifiedAt(LocalDateTime.now(), cvId);
        Cv cv = cvDao.getById(cvId);
        return new SuccessDataResult<>(Msg.DELETED.get(), cv);
    }

    private void addPropToCv(Class<?> propType, int propId, int cvId) {
        if (CandidateJobExperience.class.equals(propType)) cvDao.addJobExpToCv(propId, cvId);
        else if (CandidateLanguage.class.equals(propType)) cvDao.addLangToCv(propId, cvId);
        else if (CandidateSchool.class.equals(propType)) cvDao.addSchoolToCv(propId, cvId);
        else if (CandidateSkill.class.equals(propType)) cvDao.addSkillToCv(propId, cvId);
    }

    private void removePropFromCv(Class<?> propType, int propId, int cvId) {
        if (CandidateJobExperience.class.equals(propType)) cvDao.deleteJobExpFromCv(propId, cvId);
        else if (CandidateLanguage.class.equals(propType)) cvDao.deleteLangFromCv(propId, cvId);
        else if (CandidateSchool.class.equals(propType)) cvDao.deleteSchoolFromCv(propId, cvId);
        else if (CandidateSkill.class.equals(propType)) cvDao.deleteSkillFromCv(propId, cvId);
    }

    private Result getPropUpdateResults(int success, int fail, Map<String, Object> results, int cvId) {
        Cv cv = cvDao.getById(cvId);

        if (fail == 0) {
            return new SuccessDataResult<>(Msg.SAVED.getCustom("They are all %s ✅"), cv);
        } else if (success == 0) {
            results.put("cv", cv);
            return new ErrorDataResult<>(Msg.SAVED.getCustom("None %s ❌"), results);
        }

        String resultSum = ("Success Rate: %" + String.format("%.2f", ((double) success / (fail + success) * 100))) + String.format(" (Successful -> ✅%d  Failed -> ❌%d)", success, fail);
        if (success * 0.7 >= fail) {
            results.put(Msg.RESULT_SUM.get(), resultSum);
            results.put("cv", cv);
            return new SuccessDataResult<>(Msg.SUCCESS.get("Mostly"), results);
        } else {
            results.put(Msg.RESULT_SUM.get(), resultSum);
            results.put("cv", cv);
            return new ErrorDataResult<>(Msg.FAILED.get("Mostly"), results);
        }
    }

    private Result execLastUpdAct(Cv cv) {
        cv.setLastModifiedAt(LocalDateTime.now());
        Cv savedCv = cvDao.save(cv);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedCv);
    }

}
