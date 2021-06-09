package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.CandidateSchoolService;
import com.finalproject.hrmsbackend.core.utilities.Utils;
import com.finalproject.hrmsbackend.core.utilities.results.DataResult;
import com.finalproject.hrmsbackend.core.utilities.results.Result;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessDataResult;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessResult;
import com.finalproject.hrmsbackend.dataAccess.abstracts.CandidateSchoolDao;
import com.finalproject.hrmsbackend.dataAccess.abstracts.DepartmentDao;
import com.finalproject.hrmsbackend.dataAccess.abstracts.SchoolDao;
import com.finalproject.hrmsbackend.entities.concretes.CandidateSchool;
import com.finalproject.hrmsbackend.entities.concretes.Department;
import com.finalproject.hrmsbackend.entities.concretes.School;
import com.finalproject.hrmsbackend.entities.concretes.dtos.CandidateSchoolAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateSchoolManager implements CandidateSchoolService {

    private final CandidateSchoolDao candidateSchoolDao;
    private final SchoolDao schoolDao;
    private final DepartmentDao departmentDao;
    private final ModelMapper modelMapper;

    @Override
    public DataResult<List<CandidateSchool>> getAll() {
        return new SuccessDataResult<>("Success", candidateSchoolDao.findAll());
    }

    @Override
    public DataResult<List<CandidateSchool>> getAllSortedDesc() {
        Sort sort = Sort.by(Sort.Direction.DESC, "graduationYear");
        return new SuccessDataResult<>("Success", candidateSchoolDao.findAll(sort));
    }

    @Override
    public Result add(CandidateSchoolAddDto candidateSchoolAddDto) {
        CandidateSchool candidateSchool = modelMapper.map(candidateSchoolAddDto, CandidateSchool.class);

        School school = candidateSchool.getSchool();
        school.setName(Utils.formName(school.getName()));
        if (!Utils.tryToSaveIfNotExists(school, schoolDao)) {
            school.setId(schoolDao.getByName(school.getName()).getId());
        }

        Department department = candidateSchool.getDepartment();
        department.setName(Utils.formName(department.getName()));
        if (!Utils.tryToSaveIfNotExists(department, departmentDao)) {
            department.setId(departmentDao.getByName(department.getName()).getId());
        }

        candidateSchoolDao.save(candidateSchool);
        return new SuccessResult("Success");
    }
}
