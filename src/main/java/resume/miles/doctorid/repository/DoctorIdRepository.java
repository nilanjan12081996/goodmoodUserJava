package resume.miles.doctorid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import resume.miles.doctorid.entity.DoctorIdEntity;

public interface DoctorIdRepository extends JpaRepository<DoctorIdEntity,Long> {
    Optional<DoctorIdEntity> findByDoctorId(Long id);
}
