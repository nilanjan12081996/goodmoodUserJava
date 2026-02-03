package resume.miles.awarness.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "awareness")
@Data
public class AwarenessEntity extends BaseEntity{



    @Column(name = "awareness_name")
    private String awarenessName;

    private String image;

    private String description;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "subsidebar_id")
    private Long subsidebarId;

    private Integer status;


}

