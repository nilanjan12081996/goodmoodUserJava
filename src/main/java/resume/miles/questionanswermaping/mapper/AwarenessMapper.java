package resume.miles.questionanswermaping.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import resume.miles.questionanswermaping.dto.AnswerDTO;
import resume.miles.questionanswermaping.dto.QuestionDTO;
import resume.miles.questionanswermaping.entity.AnswerEntity;
import resume.miles.questionanswermaping.entity.QuestionEntity;

@Component
public class AwarenessMapper {
 public AnswerDTO toAnswerDTO(AnswerEntity entity) {
        return AnswerDTO.builder()
                .id(entity.getId())
                .answer(entity.getAnswer())
                .point(entity.getPoint())
                .build();
    }
    public AnswerEntity toAnswerEntity(AnswerDTO dto, QuestionEntity question) {
        return AnswerEntity.builder()
                
                .answer(dto.getAnswer())
                .point(dto.getPoint())
                .status(1)
                .question(question)
                .build();
    }

        public QuestionDTO toQuestionDTO(QuestionEntity entity) {
        return QuestionDTO.builder()
                .id(entity.getId())
                .question(entity.getQuestion())
                .answers(
                        entity.getAnswers()
                                .stream()
                                .filter(a -> a.getStatus() == 1)
                                .map(this::toAnswerDTO)
                                .toList()
                )
                .build();
    }
     public QuestionEntity toQuestionEntity(QuestionDTO dto) {
        QuestionEntity question = QuestionEntity.builder()
               
                .question(dto.getQuestion())
                .status(1)
                .build();

        if (dto.getAnswers() != null) {
            List<AnswerEntity> answers = dto.getAnswers()
                    .stream()
                    .map(a -> toAnswerEntity(a, question))
                    .toList();
            question.setAnswers(answers);
        }

        return question;
    }
}
