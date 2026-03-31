package resume.miles.supportcategory.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Long parentId;
    private Integer status;
}
