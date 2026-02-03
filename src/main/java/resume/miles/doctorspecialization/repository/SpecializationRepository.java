package resume.miles.doctorspecialization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.doctorspecialization.entity.DoctorSpecializationEntity;

@Repository
public interface SpecializationRepository extends JpaRepository<DoctorSpecializationEntity,Long> {
    Optional<DoctorSpecializationEntity> findByDoctorIdAndSpecializationId(Long doctorId,Long id);
}
