package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.CandidateCv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateCvDao extends JpaRepository<CandidateCv, Integer> {
}
