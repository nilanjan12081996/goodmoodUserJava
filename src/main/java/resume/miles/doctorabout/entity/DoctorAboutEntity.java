package resume.miles.doctorabout.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.doctorregister.entity.DoctorEntity;


@Entity
@Table(name="doctorabouts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAboutEntity extends BaseEntity{
     @Column(name = "exp", nullable = false)
    private Integer exp;

    @Column(name = "language")
    private String language;

    @Column(name = "about", columnDefinition = "LONGTEXT", nullable = false)
    private String about;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @OneToOne
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false) 
    private DoctorEntity doctor;
}
