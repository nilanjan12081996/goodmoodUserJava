package resume.miles.blogs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.blogs.entity.BlogEntity;

@Repository("userBlogsRepository")
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByAwarenessIdAndStatus(Long awarenessId, Integer status);
}
