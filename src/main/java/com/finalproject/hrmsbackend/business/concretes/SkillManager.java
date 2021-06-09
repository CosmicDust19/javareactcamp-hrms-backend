package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.SkillService;
import com.finalproject.hrmsbackend.core.utilities.results.DataResult;
import com.finalproject.hrmsbackend.core.utilities.results.Result;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessDataResult;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessResult;
import com.finalproject.hrmsbackend.dataAccess.abstracts.SkillDao;
import com.finalproject.hrmsbackend.entities.concretes.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillManager implements SkillService {

    private final SkillDao skillDao;

    @Override
    public DataResult<List<Skill>> getAll() {
        return new SuccessDataResult<>("Success", skillDao.findAll());
    }

    @Override
    public Result add(Skill skill) {
        skillDao.save(skill);
        return new SuccessResult("Success");
    }
}
