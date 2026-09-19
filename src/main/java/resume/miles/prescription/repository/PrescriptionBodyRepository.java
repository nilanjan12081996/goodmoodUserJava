package resume.miles.prescription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resume.miles.prescription.entity.PrescriptionBodyEntity;

public interface PrescriptionBodyRepository extends JpaRepository<PrescriptionBodyEntity, Long> {
}
