package resume.miles.questionanswermaping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.questionanswermaping.entity.AwarenessQuestionMapEntity;

@Repository
public interface AwarenessQuestionMapRepository extends JpaRepository<AwarenessQuestionMapEntity, Long>{
List<AwarenessQuestionMapEntity> findByAwarenessIdAndStatus(
            Long awarenessId,
            Integer status
    );
}
