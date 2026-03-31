package resume.miles.supportcategory.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

public class SupportCategorySpecification {
    public static Specification<SupportCategoryEntity> hasParentIdZero() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("parentId"), 0L);
    }

    public static Specification<SupportCategoryEntity> hasParentId(Long parentId) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("parentId"), parentId);
    }
}
