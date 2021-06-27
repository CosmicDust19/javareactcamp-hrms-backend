package com.finalproject.hrmsbackend.dataAccess.abstracts;

import com.finalproject.hrmsbackend.entities.concretes.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDao extends JpaRepository<Candidate,Integer> {
    boolean existsByEmailAndPassword(String email, String password);

    Candidate getByEmailAndPassword(String email, String password);
}
