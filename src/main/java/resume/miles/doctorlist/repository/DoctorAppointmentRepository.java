package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorAppointmentEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<DoctorAppointmentEntity, Long> {
    boolean existsByDoctorIdAndDateAndTimeonlyAndStatus(Long doctorId, LocalDate date, LocalTime timeonly, Long status);
    boolean existsByUserIdAndDateAndTimeonlyAndStatus(Long userId, LocalDate date, LocalTime timeonly, Long status);
    List<DoctorAppointmentEntity> findByDoctorIdAndDateAndStatus(Long doctorId, LocalDate date, Long status);
    List<DoctorAppointmentEntity> findByUserIdAndDateAndStatus(Long userId, LocalDate date, Long status);
    List<DoctorAppointmentEntity> findByUserIdAndDateGreaterThanEqualAndIsCompleteAndStatusOrderByDateAscTimeonlyAsc(Long userId, LocalDate date, Integer isComplete, Long status);
    List<DoctorAppointmentEntity> findByUserIdAndIsCompleteAndStatusOrderByDateDescTimeonlyDesc(Long userId, Integer isComplete, Long status);
    List<DoctorAppointmentEntity> findByUserIdAndDateLessThanAndIsCompleteAndStatusOrderByDateDescTimeonlyDesc(Long userId, LocalDate date, Integer isComplete, Long status);
}
