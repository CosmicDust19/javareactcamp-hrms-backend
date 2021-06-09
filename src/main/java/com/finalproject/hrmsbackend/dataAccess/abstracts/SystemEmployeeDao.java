package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.SystemEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemEmployeeDao extends JpaRepository<SystemEmployee,Integer> {
}
