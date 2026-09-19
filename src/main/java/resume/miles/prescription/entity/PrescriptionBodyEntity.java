package resume.miles.prescription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity(name = "prescriptionBodyEntity")
@Table(name = "prescription_body")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionBodyEntity extends BaseEntity {

    @Column(name = "header_description")
    private String header;

    @Column(name = "footer_description")
    private String footer;

    @Column(name = "logo_url")
    private String logoUrl;
}
