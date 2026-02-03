package resume.miles.doctorid.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.doctorregister.entity.DoctorEntity;

@Entity
@Table(name = "doctorids")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorIdEntity extends BaseEntity {

    @Column(name = "id_name", nullable = false)
    private String idName;

    @Column(name = "id_image")
    private String idImage;

   
    @Column(name = "registartion_no", nullable = false)
    private String registrationNo;

    @Column(name = "registartion_no_image")
    private String registrationNoImage;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "status", nullable = false)
    private Integer status = 1; // Default value based on changelog


    @OneToOne
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false) 
    private DoctorEntity doctor;
}
