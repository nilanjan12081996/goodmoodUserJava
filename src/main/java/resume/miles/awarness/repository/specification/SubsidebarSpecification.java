package resume.miles.awarness.repository.specification;









import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import resume.miles.awarness.entity.SubSidebarEntity;

public class SubsidebarSpecification {

    public static Specification<SubSidebarEntity> awarenessListing() {
        return (root, query, cb) -> {
            query.distinct(true);

            Join<Object, Object> awarenessJoin =
                root.join("awarenessList", JoinType.LEFT);

           if(query.getResultType() != Long.class){
                 root.fetch("awarenessList", JoinType.LEFT);
           }

            // join SubSidebar → Awareness
            

            return cb.and(
                cb.equal(root.get("status"), 1),
                cb.isNotNull(awarenessJoin.get("id"))
        );
        };
    }
}
