package resume.miles.doctorspecialization.mapper;

import resume.miles.doctorspecialization.dto.DoctorSpecializationDTO;
import resume.miles.doctorspecialization.entity.DoctorSpecializationEntity;

public class DoctorSpecializationMapper {

    
    private DoctorSpecializationMapper() {}

    public static DoctorSpecializationDTO toDto(DoctorSpecializationEntity entity) {
        if (entity == null) {
            return null;
        }

        return DoctorSpecializationDTO.builder()
                .id(entity.getId())
                .specializationId(entity.getSpecializationId())
                .doctorId(entity.getDoctorId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static DoctorSpecializationEntity toEntity(DoctorSpecializationDTO dto) {
        if (dto == null) {
            return null;
        }

        DoctorSpecializationEntity entity = DoctorSpecializationEntity.builder()
                .specializationId(dto.getSpecializationId())
                .doctorId(dto.getDoctorId())
                .status(dto.getStatus())
                .build();

       
        entity.setId(dto.getId());

        return entity;
    }
}
