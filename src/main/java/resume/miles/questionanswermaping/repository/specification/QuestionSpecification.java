package resume.miles.questionanswermaping.repository.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import resume.miles.questionanswermaping.entity.QuestionEntity;

public class QuestionSpecification {
 public static Specification<QuestionEntity> hasStatus(Integer status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }
    public static Specification<QuestionEntity> hasIds(List<Long> ids) {
        return (root, query, cb) ->
                root.get("id").in(ids);
    }
    public static Specification<QuestionEntity> fetchAnswers() {
        return (root, query, cb) -> {
            root.fetch("answers", JoinType.LEFT);
            query.distinct(true);
            return cb.conjunction();
        };
    }

}
