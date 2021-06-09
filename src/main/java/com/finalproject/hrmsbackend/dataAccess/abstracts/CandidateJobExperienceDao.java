package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.CandidateJobExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateJobExperienceDao extends JpaRepository<CandidateJobExperience, Integer> {
}
