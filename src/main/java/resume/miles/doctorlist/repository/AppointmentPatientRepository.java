package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.AppointmentPatientEntity;
import java.util.Optional;

@Repository
public interface AppointmentPatientRepository extends JpaRepository<AppointmentPatientEntity, Long> {
    Optional<AppointmentPatientEntity> findByAppointmentId(Long appointmentId);
}
