package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.CandidateCvService;
import com.finalproject.hrmsbackend.core.utilities.results.DataResult;
import com.finalproject.hrmsbackend.core.utilities.results.Result;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessDataResult;
import com.finalproject.hrmsbackend.core.utilities.results.SuccessResult;
import com.finalproject.hrmsbackend.dataAccess.abstracts.CandidateCvDao;
import com.finalproject.hrmsbackend.entities.concretes.CandidateCv;
import com.finalproject.hrmsbackend.entities.concretes.dtos.CandidateCvAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateCvManager implements CandidateCvService {

    private final CandidateCvDao candidateCvDao;
    private final ModelMapper modelMapper;

    @Override
    public DataResult<List<CandidateCv>> getAll() {
        return new SuccessDataResult<>("Success", candidateCvDao.findAll());
    }

    @Override
    public Result add(CandidateCvAddDto candidateCvAddDto) {
        CandidateCv candidateCv = modelMapper.map(candidateCvAddDto, CandidateCv.class);
        candidateCvDao.save(candidateCv);
        return new SuccessResult("Success");
    }

}
