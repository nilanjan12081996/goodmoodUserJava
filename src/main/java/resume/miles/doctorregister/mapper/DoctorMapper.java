package resume.miles.doctorregister.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import resume.miles.doctorabout.mapper.DoctorAboutMapper;
import resume.miles.doctorbankaccount.mapper.DoctorBankAccountMapper;
import resume.miles.doctorid.mapper.DoctorIdMapper;
import resume.miles.doctorregister.dto.DoctorDto;

import resume.miles.doctorregister.entity.DoctorEntity;

public class DoctorMapper {
    public DoctorMapper(){
        
    }

    public static DoctorDto toDto(DoctorEntity entity) {
        if (entity == null) return null;

        return DoctorDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .mobile(entity.getMobile())
                .avatar(entity.getAvatar())
                .status(entity.getStatus())
                .adminStatus(entity.getAdminStatus())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    public static DoctorDto toDtoProfile(DoctorEntity entity) {
        if (entity == null) return null;

        return DoctorDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .mobile(entity.getMobile())
                .avatar(entity.getAvatar())
                .otp(entity.getOtp())
                .otpExpire(entity.getOtpExpire())
                .oAuth(entity.getOAuth())
                .oauthProvider(entity.getOauthProvider())
                .status(entity.getStatus())
                .adminStatus(entity.getAdminStatus())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .doctorAbout(DoctorAboutMapper.toDto(entity.getDoctorAbout()))
                .doctorIds(DoctorIdMapper.toDto(entity.getDoctorIds()))
                .doctorAccounts(entity.getDoctorAccounts() != null 
                ? entity.getDoctorAccounts().stream()
                      .map(DoctorBankAccountMapper::toDTO) 
                      .collect(Collectors.toList())
                : Collections.emptyList())
                .build();
    }

    public static DoctorEntity toEntity(DoctorDto dto) {
        if (dto == null) return null;

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(dto.getId()); // ID inherited from BaseEntity
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPassword(dto.getPassword());
        doctor.setMobile(dto.getMobile());
        doctor.setAvatar(dto.getAvatar());
        doctor.setOtp(dto.getOtp());
        doctor.setOtpExpire(dto.getOtpExpire());
        doctor.setOAuth(dto.getOAuth());
        doctor.setOauthProvider(dto.getOauthProvider());
        doctor.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        doctor.setAdminStatus(dto.getAdminStatus() != null ? dto.getAdminStatus() : 0);
        doctor.setIsDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : 0);
        return doctor;
    }


    public static DoctorEntity toEntityReg(String mobile) {
           DoctorEntity doc = DoctorEntity.builder()
                              .firstName("goodmood")
                              .lastName("doctor")
                              .adminStatus(0)
                              .isDeleted(0)
                              .mobile(mobile)
                              .status(1)
                              .build();
            return doc;
    }
}
