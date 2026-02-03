package resume.miles.doctorbankaccount.entity;


import jakarta.persistence.*;
import lombok.*;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.doctorregister.entity.DoctorEntity; 



@Entity
@Table(name = "doctor_bank_account_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorBankAccountEntity extends BaseEntity{
  
    @Column(name = "account_on") 
    private String accountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(nullable = false)
    private String upi;

   
    @Column(name = "doctor_id",nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "is_main", nullable = false, columnDefinition = "tinyint")
    private Integer isMain;

     @ManyToOne(cascade = CascadeType.ALL) // or @OneToOne, depending on your logic
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private DoctorEntity doctor;
}
