package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorSlotTimingEntity;

@Repository
public interface DoctorSlotTimingRepository extends JpaRepository<DoctorSlotTimingEntity, Long> {
}
