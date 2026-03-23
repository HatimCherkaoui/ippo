package com.eckmo.ippo.repository;

import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findByUser_Email(String email);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
}
