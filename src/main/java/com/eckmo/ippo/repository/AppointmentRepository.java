package com.eckmo.ippo.repository;

import com.eckmo.ippo.domain.entity.Appointment;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDoctorAndScheduledAtBetweenOrderByScheduledAt(
            Doctor doctor, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT a FROM Appointment a
            WHERE a.doctor = :doctor
              AND a.status NOT IN ('CANCELLED', 'COMPLETED')
              AND a.scheduledAt < :end
              AND a.scheduledAt > :start
            """)
    List<Appointment> findOverlapping(@Param("doctor") Doctor doctor,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}
