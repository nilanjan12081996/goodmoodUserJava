package resume.miles.doctorlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.doctorlist.dto.DoctorListDTO;
import resume.miles.doctorlist.entity.DoctorEntity;
import resume.miles.doctorlist.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Transactional(readOnly = true)
    public List<DoctorListDTO> getAllDoctors() {
        // Here we use HQL method to avoid N+1 queries.
        // We could also use doctorRepository.findAll(DoctorSpecification.isActiveDoctor()) but that would lazily load the collections
        List<DoctorEntity> doctors = doctorRepository.findAllActiveDoctorsWithDetails();
        
        return doctors.stream().map(doctor -> {
            Integer experience = 0;
            String languages = "";
            if (doctor.getDoctorAbout() != null) {
                experience = doctor.getDoctorAbout().getExp();
                languages = doctor.getDoctorAbout().getLanguage();
            }

            List<String> specializations = new ArrayList<>();
            if (doctor.getDoctorSpecializations() != null) {
                specializations = doctor.getDoctorSpecializations().stream()
                    .filter(ds -> ds.getSpecialization() != null)
                    .map(ds -> ds.getSpecialization().getName())
                    .collect(Collectors.toList());
            }

            String fullName = doctor.getFirstName() + " " + (doctor.getLastName() != null ? doctor.getLastName() : "");
            
            return DoctorListDTO.builder()
                .id(doctor.getId())
                .name(fullName.trim())
                .avatar(doctor.getAvatar())
                .experience(experience)
                .languages(languages)
                .specializations(specializations)
                .build();
        }).collect(Collectors.toList());
    }
}
