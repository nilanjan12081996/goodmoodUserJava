package resume.miles.specialization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.specialization.entity.SpecializationEntity;

@Repository
public interface SpecilizationRepository extends JpaRepository<SpecializationEntity,Long> {
}
