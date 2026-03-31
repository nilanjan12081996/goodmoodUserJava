package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "days")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_name")
    private String dayName;
    
    @Column(name = "short_name")
    private String shortName;
    
    private Integer status;
}
