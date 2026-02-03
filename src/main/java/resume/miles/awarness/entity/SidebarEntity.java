package resume.miles.awarness.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "sidebar")
@Data
public class SidebarEntity extends BaseEntity{
    @Column(name = "sidebar_name")
    private String sidebarName;

    @Column(name = "sidebar_short_name")
    private String sidebarShortName;

    private Integer status;

}

