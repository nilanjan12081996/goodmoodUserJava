package resume.miles.doctorlist.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorlist.entity.DoctorEntity;

public class DoctorSpecification {

    public static Specification<DoctorEntity> isActiveDoctor() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("doctorAbout", jakarta.persistence.criteria.JoinType.LEFT);
                jakarta.persistence.criteria.Fetch<DoctorEntity, resume.miles.doctorlist.entity.DoctorSpecializationEntity> dsFetch = root.fetch("doctorSpecializations", jakarta.persistence.criteria.JoinType.LEFT);
                dsFetch.fetch("specialization", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("doctorEducations", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("doctorServices", jakarta.persistence.criteria.JoinType.LEFT);
            }
            return cb.equal(root.get("status"), 1);
        };
    }

    public static Specification<DoctorEntity> hasDoctorId(Long doctorId) {
        return (root, query, cb) -> cb.equal(root.get("id"), doctorId);
    }

    public static Specification<DoctorEntity> hasSupportId(Long supportId) {
        return (root, query, cb) -> {
            if (supportId == null) {
                return null;
            }
            return cb.equal(root.join("doctorSupportMaps").get("supportCategory").get("id"), supportId);
        };
    }
}
