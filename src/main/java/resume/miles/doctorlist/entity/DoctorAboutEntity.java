package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "doctorabouts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAboutEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer exp;
    
    private String language;
    
    private String about;
    
    private Integer status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @ToString.Exclude
    private DoctorEntity doctor;
}
