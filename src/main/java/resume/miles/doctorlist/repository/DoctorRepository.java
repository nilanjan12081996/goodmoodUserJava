package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorEntity;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long>, JpaSpecificationExecutor<DoctorEntity> {
    
    // HQL query avoiding raw SQL
    @Query("SELECT d FROM DoctorEntity d LEFT JOIN FETCH d.doctorAbout LEFT JOIN FETCH d.doctorSpecializations ds LEFT JOIN FETCH ds.specialization LEFT JOIN FETCH d.doctorEducations WHERE d.status = 1")
    List<DoctorEntity> findAllActiveDoctorsWithDetails();

    @Query("SELECT d FROM DoctorEntity d LEFT JOIN FETCH d.doctorAbout LEFT JOIN FETCH d.doctorSpecializations ds LEFT JOIN FETCH ds.specialization LEFT JOIN FETCH d.doctorEducations WHERE d.status = 1 AND d.id = :id")
    DoctorEntity findActiveDoctorByIdWithDetails(Long id);
}
