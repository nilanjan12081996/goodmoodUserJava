package resume.miles.doctorlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.doctorlist.dto.DoctorDetailsDTO;
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

    @Transactional(readOnly = true)
    public DoctorDetailsDTO getDoctorDetails(Long id) {
        DoctorEntity doctor = doctorRepository.findActiveDoctorByIdWithDetails(id);
        
        if (doctor == null) {
            throw new RuntimeException("Doctor not found with ID: " + id);
        }

        Integer experience = 0;
        String languages = "";
        String about = "";
        
        if (doctor.getDoctorAbout() != null) {
            experience = doctor.getDoctorAbout().getExp();
            languages = doctor.getDoctorAbout().getLanguage();
            about = doctor.getDoctorAbout().getAbout();
        }

        List<String> specializations = new ArrayList<>();
        if (doctor.getDoctorSpecializations() != null) {
            specializations = doctor.getDoctorSpecializations().stream()
                .filter(ds -> ds.getSpecialization() != null)
                .map(ds -> ds.getSpecialization().getName())
                .collect(Collectors.toList());
        }

        List<String> validEducations = new ArrayList<>();
        if (doctor.getDoctorEducations() != null) {
            validEducations = doctor.getDoctorEducations().stream()
                .map(edu -> edu.getDegree() + " in " + edu.getCourse() + " - " + edu.getInstitute())
                .collect(Collectors.toList());
        }

        String fullName = doctor.getFirstName() + " " + (doctor.getLastName() != null ? doctor.getLastName() : "");
        
        return DoctorDetailsDTO.builder()
            .id(doctor.getId())
            .name(fullName.trim())
            .avatar(doctor.getAvatar())
            .experience(experience)
            .languages(languages)
            .about(about)
            .specializations(specializations)
            .education(validEducations)   // Inserted actual mapped education logic
            .price(0)                     // Placeholder, waiting for price instructions
            .reviews(0)                   // Placeholder, waiting for reviews schema
            .rating(0.0)                  // Placeholder, waiting for rating metrics
            .build();
    }
}
