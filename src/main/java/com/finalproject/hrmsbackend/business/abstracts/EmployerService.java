package com.finalproject.hrmsbackend.business.abstracts;

import com.finalproject.hrmsbackend.core.business.UserService;
import com.finalproject.hrmsbackend.core.utilities.results.DataResult;
import com.finalproject.hrmsbackend.core.utilities.results.Result;
import com.finalproject.hrmsbackend.entities.concretes.Employer;
import com.finalproject.hrmsbackend.entities.concretes.dtos.EmployerAddDto;

public interface EmployerService extends UserService<Employer> {

    boolean existsByEmailAndPassword(String email, String password);

    DataResult<Employer> getById(int id);

    DataResult<Employer> getByEmailAndPassword(String email, String password);

    Result add(EmployerAddDto employerAddDto);

}
