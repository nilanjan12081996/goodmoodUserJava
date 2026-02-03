package resume.miles.doctorabout.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.doctorabout.entity.DoctorAboutEntity;

@Repository
public interface DoctorAboutRepository extends JpaRepository<DoctorAboutEntity,Long>{
        Optional<DoctorAboutEntity> findByDoctorId(Long id);
}
