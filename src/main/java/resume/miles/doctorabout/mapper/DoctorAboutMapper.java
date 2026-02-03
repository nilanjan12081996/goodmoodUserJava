package resume.miles.doctorabout.mapper;

import resume.miles.doctorabout.dto.DoctorAboutDTO;
import resume.miles.doctorabout.entity.DoctorAboutEntity;

public class DoctorAboutMapper {

    public static DoctorAboutDTO toDto(DoctorAboutEntity doctorAboutEntity) {
        if (doctorAboutEntity == null) {
            return null;
        }

        return DoctorAboutDTO.builder()
                .id(doctorAboutEntity.getId())
                .exp(doctorAboutEntity.getExp())
                .language(doctorAboutEntity.getLanguage())
                .about(doctorAboutEntity.getAbout())
                .doctorId(doctorAboutEntity.getDoctorId())
                .status(doctorAboutEntity.getStatus())
                .build();
    }

     public static DoctorAboutEntity toDoctorAboutEntity(DoctorAboutDTO doctorAboutDTO){
      DoctorAboutEntity data =  DoctorAboutEntity.builder()
               
                .exp(doctorAboutDTO.getExp())
                .language(doctorAboutDTO.getLanguage())
                .about(doctorAboutDTO.getAbout())
                .doctorId(doctorAboutDTO.getDoctorId())
                .status(doctorAboutDTO.getStatus())
                .build();

            data.setId(doctorAboutDTO.getId());

            return data;
 }
}
