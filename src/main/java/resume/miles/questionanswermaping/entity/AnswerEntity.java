package resume.miles.questionanswermaping.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntity extends BaseEntity{
     private String answer;

    private Integer point;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
}
