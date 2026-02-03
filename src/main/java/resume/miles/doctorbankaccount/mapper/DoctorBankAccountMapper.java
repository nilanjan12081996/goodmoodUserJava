package resume.miles.doctorbankaccount.mapper;



import resume.miles.doctorbankaccount.dto.DoctorBankAccountDTO;
import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;

public class DoctorBankAccountMapper {

    // Prevent instantiation
    private DoctorBankAccountMapper() {}

    /**
     * Converts DTO to Entity.
     * @param dto The data from the frontend
     * @param doctorId The ID of the doctor (usually from Token/SecurityContext)
     * @return Prepared Entity object
     */
    public static DoctorBankAccountEntity toEntity(DoctorBankAccountDTO dto, Long doctorId) {
        if (dto == null) {
            return null;
        }

        return DoctorBankAccountEntity.builder()
                // ID is usually null for new entries, handled by BaseEntity/DB
                // If this is an update, you might set dto.getId() here depending on BaseEntity type
                
                .accountNumber(dto.getAccountNumber())
                .ifscCode(dto.getIfscCode())
                .accountHolderName(dto.getAccountHolderName())
                .upi(dto.getUpi())
                .isMain(0)
                .status(dto.getStatus())
                
                // Manually setting the doctor ID since it's not in the DTO
                .doctorId(doctorId) 
                .build();
    }

    /**
     * Converts Entity to DTO (Useful for Response)
     */
    public static DoctorBankAccountDTO toDTO(DoctorBankAccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return DoctorBankAccountDTO.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .ifscCode(entity.getIfscCode())
                .accountHolderName(entity.getAccountHolderName())
                .upi(entity.getUpi())
                .isMain(entity.getIsMain())
                .status(entity.getStatus())
                .build();
    }
}
