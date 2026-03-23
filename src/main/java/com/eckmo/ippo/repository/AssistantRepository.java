package com.eckmo.ippo.repository;

import com.eckmo.ippo.domain.entity.Assistant;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {
    Optional<Assistant> findByUser(User user);
    List<Assistant> findByAssignedDoctor(Doctor doctor);
}
