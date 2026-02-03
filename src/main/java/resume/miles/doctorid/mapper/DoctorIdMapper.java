package resume.miles.doctorid.mapper;

import resume.miles.doctorid.dto.DoctorIdDTO;
import resume.miles.doctorid.entity.DoctorIdEntity;

public class DoctorIdMapper {

    // Private constructor to hide the implicit public one
    private DoctorIdMapper() {}

    public static DoctorIdDTO toDto(DoctorIdEntity doctorIdEntity) {
        if (doctorIdEntity == null) {
            return null;
        }

        return DoctorIdDTO.builder()
                .id(doctorIdEntity.getId())
                .idName(doctorIdEntity.getIdName())
                .idImage(doctorIdEntity.getIdImage())
                .registrationNo(doctorIdEntity.getRegistrationNo())
                .registrationNoImage(doctorIdEntity.getRegistrationNoImage())
                .doctorId(doctorIdEntity.getDoctorId())
                .status(doctorIdEntity.getStatus())
                .createdAt(doctorIdEntity.getCreatedAt())
                .updatedAt(doctorIdEntity.getUpdatedAt())
                .build();
    }

    public static DoctorIdEntity toEntity(DoctorIdDTO doctorIdDTO) {
        if (doctorIdDTO == null) {
            return null;
        }

        DoctorIdEntity entity = DoctorIdEntity.builder()
                .idName(doctorIdDTO.getIdName())
                .idImage(doctorIdDTO.getIdImage())
                .registrationNo(doctorIdDTO.getRegistrationNo())
                .registrationNoImage(doctorIdDTO.getRegistrationNoImage())
                .doctorId(doctorIdDTO.getDoctorId())
                .status(doctorIdDTO.getStatus())
                .build();
        
        // ID is usually set manually if it exists in DTO (e.g. for updates)
        entity.setId(doctorIdDTO.getId());

        return entity;
    }
}