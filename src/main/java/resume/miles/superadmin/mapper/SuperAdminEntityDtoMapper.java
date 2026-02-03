package resume.miles.superadmin.mapper;

import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.superadmin.entity.SuperAdmin;

public class SuperAdminEntityDtoMapper {

    public static SuperAdminResponseDTO toAdminResponseDTO(SuperAdmin superAdmin){
            SuperAdminResponseDTO superAdminResponseDTO = SuperAdminResponseDTO.builder()
                                                         .id(superAdmin.getId())
                                                         .email(superAdmin.getEmail())
                                                         .firstName(superAdmin.getFirstName())
                                                         .lastName(superAdmin.getLastName())
                                                         .mobile(superAdmin.getMobile())
                                                         .username(superAdmin.getUsername())   
                                                         .tokenType("superadmin")
                                                         .status(superAdmin.getStatus())
                                                         .build();
            return superAdminResponseDTO;
    }
}
