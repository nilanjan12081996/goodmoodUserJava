package resume.miles.doctorregister.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.doctorregister.dto.DoctorDto;
import resume.miles.doctorregister.entity.DoctorEntity;
import resume.miles.doctorregister.mapper.DoctorMapper;
import resume.miles.doctorregister.repository.DoctorRepository;

@Service
public class OtpService {
    public final DoctorRepository doctorRepository;

     public OtpService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
     }

     @Transactional
     public boolean otpGenerate(Long id){
        Integer otp = new Random().nextInt(900000) + 100000;
        LocalDateTime time = LocalDateTime.now().plusMinutes(5);
        doctorRepository.updateOtp(id,otp,time);
        return true;
     }
     public boolean check(Long id){
         doctorRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found "+id));
        return true;
     }

      public DoctorDto checkAndVerify(Long id,Integer otp){
        DoctorEntity doc =  doctorRepository.findById(id).orElseThrow(()->new RuntimeException("Id not found "+id));
        DoctorDto data = DoctorMapper.toDto(doc);
        return data;
     }
     
}
