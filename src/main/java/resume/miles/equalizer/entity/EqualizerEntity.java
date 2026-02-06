package resume.miles.equalizer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name="moodequalizers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EqualizerEntity extends BaseEntity{
    @Column(name="awareness_id",nullable = true)
    private Long awarenessId;
    @Column(name="url",nullable = false)
    private String url;
    @Column(name="type",nullable = false)
    private String type;
    @Column(name="status",nullable = false)
    private Integer status;
    @Column(name="name",nullable = false)
    private String name;
    
}
