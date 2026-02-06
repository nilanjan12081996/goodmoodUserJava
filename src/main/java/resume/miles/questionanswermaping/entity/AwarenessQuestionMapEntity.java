package resume.miles.questionanswermaping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "awareness-question-maps")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwarenessQuestionMapEntity extends BaseEntity{
     @Column(name = "awareness_id")
    private Long awarenessId;

    @Column(name = "question_id")
    private Long questionId;

    private Integer status;
}
