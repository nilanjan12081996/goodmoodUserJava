package resume.miles.equalizer.mapper;

import resume.miles.equalizer.dto.EqualizerDto;
import resume.miles.equalizer.entity.EqualizerEntity;

public class EqualizerMapper {
    public EqualizerMapper(){

    }
    public static EqualizerDto toDto(EqualizerEntity entity){
        return EqualizerDto.builder()
        .id(entity.getId())
        .awarenessId(entity.getAwarenessId())
        .url(entity.getUrl())
        .type(entity.getType())
        .status(entity.getStatus())
        .name(entity.getName())
        .build();
    }
    public static EqualizerEntity toEntity(EqualizerDto dto){
        return EqualizerEntity.builder()
        .awarenessId(dto.getAwarenessId())
        .url(dto.getUrl())
        .type(dto.getType())
        .status(dto.getStatus())
        .name((dto.getName()))
        .build();
    }
}
