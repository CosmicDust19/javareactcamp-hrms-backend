package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.CandidateLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateLanguageDao extends JpaRepository<CandidateLanguage, Integer> {
}
