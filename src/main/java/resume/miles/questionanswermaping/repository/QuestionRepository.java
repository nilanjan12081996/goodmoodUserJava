package resume.miles.questionanswermaping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.questionanswermaping.entity.QuestionEntity;

public interface QuestionRepository extends
        JpaRepository<QuestionEntity, Long>,
        JpaSpecificationExecutor<QuestionEntity>{

}
