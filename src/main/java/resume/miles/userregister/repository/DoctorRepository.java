package resume.miles.userregister.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import resume.miles.userregister.entity.UserEntity;

public interface DoctorRepository extends JpaRepository<UserEntity,Long>,JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByMobile(String mobile);

    @Modifying
    @Transactional 
    @Query("UPDATE DoctorEntity d SET d.otp = :otp, d.otpExpire = :time WHERE d.id = :id")
    Long updateOtp(@Param("id") Long id,@Param("otp") Integer otp,@Param("time") LocalDateTime time );
}
