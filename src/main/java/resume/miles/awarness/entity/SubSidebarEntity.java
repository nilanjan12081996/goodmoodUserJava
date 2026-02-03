package resume.miles.awarness.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "subsidebar")
@Data
public class SubSidebarEntity extends BaseEntity{

 

    @Column(name = "subsidebar_name")
    private String subsidebarName;

    @Column(name = "subsidebar_short_name")
    private String subsidebarShortName;

    @Column(name = "mastersidebar_id")
    private Long masterSidebarId;

    private Integer status;

 
}

