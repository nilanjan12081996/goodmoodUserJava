package resume.miles.doctorlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_name")
    private String firstName;
    
    @Column(name = "l_name")
    private String lastName;
    
    private String email;
    private String mobile;
    private String avatar;
    private Integer status;

    @OneToOne(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DoctorAboutEntity doctorAbout;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DoctorSpecializationEntity> doctorSpecializations;
}
