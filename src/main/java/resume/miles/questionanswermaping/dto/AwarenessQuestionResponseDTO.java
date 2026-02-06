package resume.miles.questionanswermaping.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwarenessQuestionResponseDTO {
    private Long awarenessId;
    private List<QuestionDTO> questions;
}
