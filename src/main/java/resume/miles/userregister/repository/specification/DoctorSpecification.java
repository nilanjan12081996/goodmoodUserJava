package resume.miles.userregister.repository.specification;

import jakarta.persistence.criteria.JoinType;
import resume.miles.userregister.entity.UserEntity;

import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public static Specification<UserEntity> byIdWithDetails(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("doctorAbout", JoinType.LEFT);
                root.fetch("doctorAccounts", JoinType.LEFT);
                root.fetch("doctorIds", JoinType.LEFT);
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }
}
