package com.finalproject.hrmsbackend.business.abstracts;

import com.finalproject.hrmsbackend.core.business.UserService;
import com.finalproject.hrmsbackend.core.utilities.results.Result;
import com.finalproject.hrmsbackend.entities.concretes.Employer;
import com.finalproject.hrmsbackend.entities.concretes.dtos.EmployerAddDto;

public interface EmployerService extends UserService<Employer> {
    Result add(EmployerAddDto employerAddDto);
}
