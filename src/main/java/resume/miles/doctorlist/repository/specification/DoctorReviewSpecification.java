package resume.miles.doctorlist.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorlist.entity.DoctorReviewEntity;
import jakarta.persistence.criteria.JoinType;

public class DoctorReviewSpecification {

    public static Specification<DoctorReviewEntity> hasDoctorId(Long doctorId) {
        return (root, query, cb) -> cb.equal(root.get("doctorId"), doctorId);
    }

    public static Specification<DoctorReviewEntity> isActive() {
        return (root, query, cb) -> cb.equal(root.get("status"), 1);
    }

    public static Specification<DoctorReviewEntity> withUser() {
        return (root, query, cb) -> {
            // Fetch the user association to avoid N+1. 
            // We check if the query is a selection query (not a count query) to avoid errors.
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT);
            }
            return cb.conjunction(); // Return an empty "always true" predicate
        };
    }
}
