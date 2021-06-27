package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.EmployerCheckService;
import com.finalproject.hrmsbackend.business.abstracts.EmployerService;
import com.finalproject.hrmsbackend.core.abstracts.EmailService;
import com.finalproject.hrmsbackend.core.utilities.results.*;
import com.finalproject.hrmsbackend.dataAccess.abstracts.EmployerDao;
import com.finalproject.hrmsbackend.entities.concretes.Employer;
import com.finalproject.hrmsbackend.entities.concretes.dtos.EmployerAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerManager implements EmployerService {

    private final EmployerDao employerDao;
    private final EmployerCheckService employerCheckService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    public DataResult<List<Employer>> getAll() {
        return new SuccessDataResult<>("Success", employerDao.findAll());
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        return employerDao.existsByEmailAndPassword(email, password);
    }

    @Override
    public DataResult<Employer> getById(int id) {
        if (!employerDao.existsById(id)){
            return new ErrorDataResult<>("id does not exist");
        }
        return new SuccessDataResult<>("Success", employerDao.getById(id));
    }

    @Override
    public DataResult<Employer> getByEmailAndPassword(String email, String password) {
        return new SuccessDataResult<>("Success", employerDao.getByEmailAndPassword(email, password));
    }

    @Override
    public Result add(EmployerAddDto employerAddDto) {
        if (!employerCheckService.isCompatibleWebSiteAndEmail(employerAddDto)) {
            return new ErrorResult("Incompatible Web Site & E-mail!");
        }
        simplifyPhoneNumber(employerAddDto);
        Employer employer = modelMapper.map(employerAddDto, Employer.class);
        emailService.sendVerificationMail(employerAddDto.getEmail());
        employerDao.save(employer);
        return new SuccessResult("Employer Saved, your account will be available after our employees confirmed you.");
    }

    public void simplifyPhoneNumber(EmployerAddDto employerAddDto) {
        String simplePhone = employerAddDto.getPhoneNumber().replaceAll("[\\s-]", "");
        if (simplePhone.length() > 11 && simplePhone.charAt(0) != '+') simplePhone = "+" + simplePhone;
        employerAddDto.setPhoneNumber(simplePhone);
    }
}
