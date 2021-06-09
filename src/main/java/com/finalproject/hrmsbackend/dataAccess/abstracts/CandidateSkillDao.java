package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateSkillDao extends JpaRepository<CandidateSkill, Integer> {
}
