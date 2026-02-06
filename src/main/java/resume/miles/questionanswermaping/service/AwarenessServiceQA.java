package resume.miles.questionanswermaping.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import resume.miles.questionanswermaping.dto.AwarenessQuestionResponseDTO;
import resume.miles.questionanswermaping.dto.QuestionDTO;
import resume.miles.questionanswermaping.entity.AwarenessQuestionMapEntity;
import resume.miles.questionanswermaping.entity.QuestionEntity;
import resume.miles.questionanswermaping.mapper.AwarenessMapper;
import resume.miles.questionanswermaping.repository.AwarenessQuestionMapRepository;
import resume.miles.questionanswermaping.repository.QuestionRepository;
import resume.miles.questionanswermaping.repository.specification.QuestionSpecification;

@Service
public class AwarenessServiceQA {
      private final AwarenessQuestionMapRepository mapRepository;
    private final QuestionRepository questionRepository;
    private final AwarenessMapper mapper;
     public AwarenessServiceQA(
            AwarenessQuestionMapRepository mapRepository,
            QuestionRepository questionRepository,
            AwarenessMapper mapper
    ) {
        this.mapRepository = mapRepository;
        this.questionRepository = questionRepository;
        this.mapper = mapper;
    }
     public AwarenessQuestionResponseDTO getQuestionsByAwarenessId(Long awarenessId) {

        // Step 1: Get mapped question IDs
        List<Long> questionIds = mapRepository
                .findByAwarenessIdAndStatus(awarenessId, 1)
                .stream()
                .map(AwarenessQuestionMapEntity::getQuestionId)
                .toList();

        if (questionIds.isEmpty()) {
            return AwarenessQuestionResponseDTO.builder()
                    .awarenessId(awarenessId)
                    .questions(List.of())
                    .build();
        }

        // Step 2: Build specification
        Specification<QuestionEntity> spec =
                Specification.where(QuestionSpecification.hasStatus(1))
                        .and(QuestionSpecification.hasIds(questionIds))
                        .and(QuestionSpecification.fetchAnswers());

        // Step 3: Fetch + map
        List<QuestionDTO> questions = questionRepository
                .findAll(spec)
                .stream()
                .map(mapper::toQuestionDTO)
                .toList();

        return AwarenessQuestionResponseDTO.builder()
                .awarenessId(awarenessId)
                .questions(questions)
                .build();
    }
}
