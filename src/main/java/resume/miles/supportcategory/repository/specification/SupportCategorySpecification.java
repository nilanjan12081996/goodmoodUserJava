package resume.miles.supportcategory.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

public class SupportCategorySpecification {
    public static Specification<SupportCategoryEntity> hasParentIdZero() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.and(
                criteriaBuilder.equal(root.get("parentId"), 0L),
                criteriaBuilder.equal(root.get("status"), 1)
            );
    }

    public static Specification<SupportCategoryEntity> hasParentId(Long parentId) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.and(
                criteriaBuilder.equal(root.get("parentId"), parentId),
                criteriaBuilder.equal(root.get("status"), 1)
            );
    }
}
