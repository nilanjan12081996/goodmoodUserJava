package resume.miles.specialization.dto;



import lombok.*;

@Data // Generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationDTO  {

    private Long id;

    private String name;

    private String description;

    private Integer status;
}
