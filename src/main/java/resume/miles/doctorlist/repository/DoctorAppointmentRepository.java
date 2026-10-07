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
    List<DoctorAppointmentEntity> findByUserIdAndStatusOrderByDateDescTimeonlyDesc(Long userId, Long status);
    List<DoctorAppointmentEntity> findByDateAndStatus(LocalDate date, Long status);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM DoctorAppointmentEntity a WHERE a.userId = :userId AND a.status = :status AND a.date >= :date AND a.isComplete = :isComplete ORDER BY a.date ASC, a.timeonly ASC")
    org.springframework.data.domain.Page<DoctorAppointmentEntity> findUpcomingAppointments(
            @org.springframework.data.repository.query.Param("userId") Long userId, 
            @org.springframework.data.repository.query.Param("date") LocalDate date, 
            @org.springframework.data.repository.query.Param("isComplete") Integer isComplete, 
            @org.springframework.data.repository.query.Param("status") Long status, 
            org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM DoctorAppointmentEntity a WHERE a.userId = :userId AND a.status = :status AND (a.isComplete = :isComplete OR (a.isComplete = :isNotComplete AND a.date < :date)) ORDER BY a.date DESC, a.timeonly DESC")
    org.springframework.data.domain.Page<DoctorAppointmentEntity> findCompletedAppointments(
            @org.springframework.data.repository.query.Param("userId") Long userId, 
            @org.springframework.data.repository.query.Param("date") LocalDate date, 
            @org.springframework.data.repository.query.Param("isComplete") Integer isComplete, 
            @org.springframework.data.repository.query.Param("isNotComplete") Integer isNotComplete,
            @org.springframework.data.repository.query.Param("status") Long status, 
            org.springframework.data.domain.Pageable pageable);
}
