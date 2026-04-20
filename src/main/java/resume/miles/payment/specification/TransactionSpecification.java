package resume.miles.payment.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorlist.entity.DoctorAppointmentEntity;
import resume.miles.doctorlist.entity.DoctorEntity;
import resume.miles.payment.entity.TransactionEntity;

public class TransactionSpecification {

    public static Specification<TransactionEntity> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            // Reusing the join for both filtering and potential fetching if needed
            // However, Specification's root.join and root.fetch can be tricky.
            // Using join for filtering by userId
            Join<TransactionEntity, DoctorAppointmentEntity> appointmentJoin = root.join("appointment", JoinType.INNER);
            
            // To optimize, we can also fetch if the result type is TransactionEntity
            if (Long.class != query.getResultType()) {
                root.fetch("appointment", JoinType.INNER).fetch("doctor", JoinType.LEFT);
            }
            
            return criteriaBuilder.equal(appointmentJoin.get("userId"), userId);
        };
    }

    public static Specification<TransactionEntity> searchByDoctorName(String doctorName) {
        return (root, query, criteriaBuilder) -> {
            if (doctorName == null || doctorName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<TransactionEntity, DoctorAppointmentEntity> appointmentJoin = root.join("appointment", JoinType.INNER);
            Join<DoctorAppointmentEntity, DoctorEntity> doctorJoin = appointmentJoin.join("doctor", JoinType.INNER);
            
            String searchPattern = "%" + doctorName.toLowerCase() + "%";
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(doctorJoin.get("firstName")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(doctorJoin.get("lastName")), searchPattern),
                criteriaBuilder.like(
                    criteriaBuilder.lower(
                        criteriaBuilder.concat(
                            criteriaBuilder.concat(doctorJoin.get("firstName"), " "),
                            doctorJoin.get("lastName")
                        )
                    ),
                    searchPattern
                )
            );

        };
    }
}
