package resume.miles.supportcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

@Repository
public interface SupportCategoryRepository extends JpaRepository<SupportCategoryEntity, Long>, JpaSpecificationExecutor<SupportCategoryEntity> {
}
