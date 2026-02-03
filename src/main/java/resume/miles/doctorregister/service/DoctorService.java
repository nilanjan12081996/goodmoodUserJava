package resume.miles.doctorregister.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.doctorregister.dto.DoctorDto;
import resume.miles.doctorregister.dto.DoctorProfileDTO;
import resume.miles.doctorregister.entity.DoctorEntity;
import resume.miles.doctorregister.mapper.DoctorMapper;
import resume.miles.doctorregister.repository.DoctorRepository;
import resume.miles.doctorregister.repository.specification.DoctorSpecification;

@Service
public class DoctorService {

     public final DoctorRepository doctorRepository;

     public final OtpService otpService;

     public DoctorService(DoctorRepository doctorRepository,OtpService otpService){
        this.doctorRepository = doctorRepository;
        this.otpService = otpService;
     }

     public String register(String mobile){
        
        DoctorEntity doctorData = doctorRepository.findByMobile(mobile)
            .orElseGet(() -> {
                DoctorEntity doctorDataSave = DoctorMapper.toEntityReg(mobile);
                return doctorRepository.save(doctorDataSave);
        });
        Long id = doctorData.getId();
        try{
            otpService.otpGenerate(id);
        }catch(Exception e){
            return e.getMessage();
        }
        
        return "Otp send";
     }

     @Transactional
     public String profile(DoctorProfileDTO doctorProfileDTO,Long id){
        DoctorEntity doctorEntityData = doctorRepository.findById(id).orElseThrow(()->new RuntimeException("no doctor found"));
        if(doctorProfileDTO.getFname() != null){
            doctorEntityData.setFirstName(doctorProfileDTO.getFname());
        }
        if(doctorProfileDTO.getLname() != null){
            doctorEntityData.setLastName(doctorProfileDTO.getLname());
        }
        if(doctorProfileDTO.getEmail() != null){
            doctorEntityData.setEmail(doctorProfileDTO.getEmail());
        }
        if(doctorProfileDTO.getDob() != null){
            doctorEntityData.setDateOfBirth(doctorProfileDTO.getDob());
        }
        if(doctorProfileDTO.getGender() != null){
            doctorEntityData.setGender(doctorProfileDTO.getGender());
        }
        return "update successfully";
     }

     
     public DoctorDto profileList(Long id){
        DoctorEntity doctor = doctorRepository.findOne(DoctorSpecification.byIdWithDetails(id)).orElseThrow(()-> new RuntimeException("invalid id"));
        DoctorDto data = DoctorMapper.toDtoProfile(doctor);
        return data;
     }
}
