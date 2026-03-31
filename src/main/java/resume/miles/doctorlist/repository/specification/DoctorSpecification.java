package resume.miles.doctorlist.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorlist.entity.DoctorEntity;

public class DoctorSpecification {

    public static Specification<DoctorEntity> isActiveDoctor() {
        return (root, query, cb) -> cb.equal(root.get("status"), 1);
    }

    public static Specification<DoctorEntity> hasDoctorId(Long doctorId) {
        return (root, query, cb) -> cb.equal(root.get("id"), doctorId);
    }
}
