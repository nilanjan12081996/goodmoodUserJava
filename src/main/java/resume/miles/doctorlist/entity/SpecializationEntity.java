package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "specializations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String des;
    
    private Integer status;
}
