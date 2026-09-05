package resume.miles.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import resume.miles.blog.entity.BlogEntity;

import java.util.List;

@Repository("userBlogRepository")
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByAwarenessId(Long awarenessId);
}
