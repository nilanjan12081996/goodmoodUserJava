package resume.miles.doctorbankaccount.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import resume.miles.doctorbankaccount.dto.DoctorBankAccountDTO;
import resume.miles.doctorbankaccount.entity.DoctorBankAccountEntity;
import resume.miles.doctorbankaccount.mapper.DoctorBankAccountMapper;
import resume.miles.doctorbankaccount.repository.DoctorServiceRepository;

@Service
@RequiredArgsConstructor
public class DoctorAccountService {
    private final DoctorServiceRepository doctorServiceRepository;

    @Transactional
    public String addUpdate(DoctorBankAccountDTO doctorBankAccountDTO,Long id){
        try{
            if(doctorBankAccountDTO.getId() != null){
                DoctorBankAccountEntity getData = doctorServiceRepository.findById(doctorBankAccountDTO.getId()).orElseThrow(()-> new IllegalArgumentException("invalid id"));
                if (doctorBankAccountDTO.getAccountNumber() != null) {
                    getData.setAccountNumber(doctorBankAccountDTO.getAccountNumber());
                }
                if (doctorBankAccountDTO.getIfscCode() != null) {
                    getData.setIfscCode(doctorBankAccountDTO.getIfscCode());
                }
                if (doctorBankAccountDTO.getAccountHolderName() != null) {
                    getData.setAccountHolderName(doctorBankAccountDTO.getAccountHolderName());
                }
                if (doctorBankAccountDTO.getUpi() != null) {
                    getData.setUpi(doctorBankAccountDTO.getUpi());
                }
                if (doctorBankAccountDTO.getIsMain() != null) {
                    getData.setIsMain(doctorBankAccountDTO.getIsMain());
                }
                if (doctorBankAccountDTO.getStatus() != null) {
                    getData.setStatus(doctorBankAccountDTO.getStatus());
                }
                doctorServiceRepository.save(getData);
            }else{
                DoctorBankAccountEntity doctorBankAccountEntity = DoctorBankAccountMapper.toEntity(doctorBankAccountDTO,id);
                DoctorBankAccountEntity doctorBankAccountDataSave = doctorServiceRepository.save(doctorBankAccountEntity);
            }
        }catch(Exception e){
           if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("Internal System Error: " + e.getMessage(), e);
        }
        return "data saved";
    }


    public List<DoctorBankAccountDTO> list(Long id){
        List<DoctorBankAccountEntity> data = doctorServiceRepository.findByDoctorId(id);
        List<DoctorBankAccountDTO> mappedData = data.stream().map(dat->DoctorBankAccountMapper.toDTO(dat)).collect(Collectors.toList());
        return mappedData;
    }

    @Transactional
    public String primary(Long id,Long userId){
        List<DoctorBankAccountEntity> data = doctorServiceRepository.findByDoctorId(userId);
        for (DoctorBankAccountEntity account : data) {
            if (account.getIsMain().equals(1)) {
                account.setIsMain(0); 
            }
        }
        doctorServiceRepository.resetAllToSecondary(userId);
        DoctorBankAccountEntity findData = doctorServiceRepository.findById(id).orElseThrow(()->new IllegalArgumentException("invalid id"));
        findData.setIsMain(1);
        doctorServiceRepository.save(findData);
        return "updated";
    }
}
