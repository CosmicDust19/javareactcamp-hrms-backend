package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.CandidateSchool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateSchoolDao extends JpaRepository<CandidateSchool, Integer> {
}
