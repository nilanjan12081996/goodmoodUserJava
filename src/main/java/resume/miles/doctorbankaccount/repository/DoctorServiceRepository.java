package resume.miles.doctorbankaccount.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;

@Repository
public interface DoctorServiceRepository extends JpaRepository<DoctorBankAccountEntity,Long> {
        List<DoctorBankAccountEntity> findByDoctorId(Long id);
        @Modifying // Tells Spring this is an UPDATE/DELETE, not a SELECT
        @Query("UPDATE DoctorBankAccountEntity d SET d.isMain = 0 WHERE d.doctorId = :doctorId")
        void resetAllToSecondary(Long doctorId);
} 