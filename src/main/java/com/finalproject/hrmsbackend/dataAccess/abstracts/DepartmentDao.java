package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentDao extends JpaRepository<Department, Short> {
    Department getByName(String name);
}