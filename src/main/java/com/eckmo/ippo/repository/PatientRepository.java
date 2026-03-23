package com.eckmo.ippo.repository;

import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByUser_Email(String email);
}
