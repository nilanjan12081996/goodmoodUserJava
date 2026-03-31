package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorTimeslotEntity;

import java.util.List;

@Repository
public interface DoctorTimeslotRepository extends JpaRepository<DoctorTimeslotEntity, Long> {
    
    // Fetch slots eagerly with days for a specific doctor
    @Query("SELECT dt FROM DoctorTimeslotEntity dt JOIN FETCH dt.day d WHERE dt.status = 1 AND dt.doctorId = :doctorId ORDER BY d.id, dt.startTime")
    List<DoctorTimeslotEntity> findActiveTimeslotsByDoctorIdWithDays(Long doctorId);
}
