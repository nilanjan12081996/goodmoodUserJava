package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorReviewEntity;

@Repository
public interface DoctorReviewRepository extends JpaRepository<DoctorReviewEntity, Long>, JpaSpecificationExecutor<DoctorReviewEntity> {
}
