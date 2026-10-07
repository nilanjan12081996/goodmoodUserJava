package resume.miles.doctorlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import resume.miles.doctorlist.entity.DoctorReviewEntity;
import java.util.List;

@Repository
public interface DoctorReviewRepository extends JpaRepository<DoctorReviewEntity, Long>, JpaSpecificationExecutor<DoctorReviewEntity> {

    @Query("SELECT COUNT(dr) FROM DoctorReviewEntity dr WHERE dr.doctorId = :doctorId AND dr.status = 1")
    Integer countActiveReviewsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT dr FROM DoctorReviewEntity dr WHERE dr.doctorId = :doctorId AND dr.userId = :userId AND dr.status = 1")
    List<DoctorReviewEntity> findByDoctorIdAndUserId(@Param("doctorId") Long doctorId, @Param("userId") Long userId);

    @Query("SELECT AVG(dr.rating) FROM DoctorReviewEntity dr WHERE dr.doctorId = :doctorId AND dr.status = 1")
    Double findAverageRatingByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT dr.doctorId, AVG(dr.rating) FROM DoctorReviewEntity dr WHERE dr.status = 1 GROUP BY dr.doctorId")
    List<Object[]> findAllAverageRatingsGroupedByDoctor();
}
