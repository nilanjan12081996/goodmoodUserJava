package resume.miles.doctorregister.repository.specification;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorregister.entity.DoctorEntity;

public class DoctorSpecification {

    public static Specification<DoctorEntity> byIdWithDetails(Long id) {
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
