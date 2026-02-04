package resume.miles.posts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import resume.miles.posts.entity.PostEntity;

@Repository
public interface PostRepository
        extends JpaRepository<PostEntity, Long>,
                JpaSpecificationExecutor<PostEntity> {
}
