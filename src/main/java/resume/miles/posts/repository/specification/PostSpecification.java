package resume.miles.posts.repository.specification;
import org.springframework.data.jpa.domain.Specification;


import resume.miles.posts.entity.PostEntity;

public class PostSpecification {
    public PostSpecification(){

    }

    public static Specification<PostEntity> publishedPosts() {
        return (root, query, cb) -> {

            // ORDER BY (safe)
            query.orderBy(cb.desc(root.get("publishedAt")));

            // WHERE status = 1
            return cb.equal(root.get("status"), 1);
        };
    }
}


