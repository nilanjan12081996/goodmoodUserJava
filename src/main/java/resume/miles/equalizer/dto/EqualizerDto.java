package resume.miles.equalizer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EqualizerDto {
        private Long id;
        private Long awarenessId;
        private String url;
        private String type;
        private Integer status;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt; 
}
