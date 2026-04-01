package resume.miles.doctorlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.doctorlist.dto.AddDoctorReviewDTO;
import resume.miles.doctorlist.dto.DoctorAvailabilityDTO;
import resume.miles.doctorlist.dto.DoctorDetailsDTO;
import resume.miles.doctorlist.dto.DoctorListDTO;
import resume.miles.doctorlist.dto.DoctorReviewDTO;
import resume.miles.doctorlist.entity.DoctorEntity;
import resume.miles.doctorlist.entity.DoctorTimeslotEntity;
import resume.miles.doctorlist.entity.DoctorSlotTimingEntity;
import resume.miles.doctorlist.entity.DoctorTimeslotEntity;
import resume.miles.doctorlist.entity.DoctorReviewEntity;
import resume.miles.doctorlist.entity.DoctorServiceEntity;
import resume.miles.doctorlist.repository.DoctorRepository;
import resume.miles.doctorlist.repository.DoctorSlotTimingRepository;
import resume.miles.doctorlist.repository.DoctorTimeslotRepository;
import resume.miles.doctorlist.repository.DoctorReviewRepository;
import resume.miles.doctorlist.repository.ContractTypeRepository;
import resume.miles.doctorlist.entity.ContractTypeEntity;
import resume.miles.doctorlist.dto.DoctorPackageDTO;
import resume.miles.doctorlist.repository.specification.DoctorReviewSpecification;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorTimeslotRepository doctorTimeslotRepository;
    private final DoctorSlotTimingRepository doctorSlotTimingRepository;
    private final DoctorReviewRepository doctorReviewRepository;
    private final ContractTypeRepository contractTypeRepository;

    public DoctorService(DoctorRepository doctorRepository, 
                         DoctorTimeslotRepository doctorTimeslotRepository,
                         DoctorSlotTimingRepository doctorSlotTimingRepository,
                         DoctorReviewRepository doctorReviewRepository,
                         ContractTypeRepository contractTypeRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorTimeslotRepository = doctorTimeslotRepository;
        this.doctorSlotTimingRepository = doctorSlotTimingRepository;
        this.doctorReviewRepository = doctorReviewRepository;
        this.contractTypeRepository = contractTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<DoctorListDTO> getAllDoctors(Long supportId) {
        Specification<DoctorEntity> spec = Specification.where(resume.miles.doctorlist.repository.specification.DoctorSpecification.isActiveDoctor());
        
        if (supportId != null) {
            spec = spec.and(resume.miles.doctorlist.repository.specification.DoctorSpecification.hasSupportId(supportId));
        }

        List<DoctorEntity> doctors = doctorRepository.findAll(spec);
        
        // Fetch average ratings for all doctors to avoid N+1
        List<Object[]> ratingData = doctorReviewRepository.findAllAverageRatingsGroupedByDoctor();
        Map<Long, Double> ratingMap = ratingData.stream()
            .collect(Collectors.toMap(
                data -> (Long) data[0],
                data -> (Double) data[1]
            ));

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
            Double avgRating = ratingMap.getOrDefault(doctor.getId(), 0.0);

            // Price Extraction logic (new)
            Double videoPrice = 0.0;
            Double voicePrice = 0.0;
            
            if (doctor.getDoctorServices() != null && !doctor.getDoctorServices().isEmpty()) {
                // Try choosing service matching supportId
                DoctorServiceEntity selectedService = null;
                if (supportId != null) {
                    selectedService = doctor.getDoctorServices().stream()
                        .filter(ds -> supportId.equals(ds.getSupportCategoryId()))
                        .findFirst().orElse(null);
                }
                
                // Fallback to first available service if no match
                if (selectedService == null) {
                    selectedService = doctor.getDoctorServices().iterator().next();
                }

                if (selectedService != null) {
                    videoPrice = selectedService.getVideoCallPrice();
                    voicePrice = selectedService.getVoiceCallPrice();
                }
            }

            return DoctorListDTO.builder()
                .id(doctor.getId())
                .name(fullName.trim())
                .avatar(doctor.getAvatar())
                .experience(experience)
                .languages(languages)
                .specializations(specializations)
                .rating(avgRating)
                .videoCallPrice(videoPrice)
                .voiceCallPrice(voicePrice)
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
        
        Integer reviewCount = doctorReviewRepository.countActiveReviewsByDoctorId(id);
        Double avgRating = doctorReviewRepository.findAverageRatingByDoctorId(id);
        if (avgRating == null) avgRating = 0.0;

        Double videoPrice = 0.0;
        Double voicePrice = 0.0;
        if (doctor.getDoctorServices() != null && !doctor.getDoctorServices().isEmpty()) {
            DoctorServiceEntity service = doctor.getDoctorServices().iterator().next();
            videoPrice = service.getVideoCallPrice();
            voicePrice = service.getVoiceCallPrice();
        }

        return DoctorDetailsDTO.builder()
            .id(doctor.getId())
            .name(fullName.trim())
            .avatar(doctor.getAvatar())
            .experience(experience)
            .languages(languages)
            .about(about)
            .specializations(specializations)
            .education(validEducations)   // Inserted actual mapped education logic
            .videoCallPrice(videoPrice)
            .voiceCallPrice(voicePrice)
            .reviews(reviewCount)
            .rating(avgRating)
            .build();
    }

    @Transactional(readOnly = true)
    public List<DoctorAvailabilityDTO> getDoctorAvailability(Long doctorId) {
        List<DoctorTimeslotEntity> timeslots = doctorTimeslotRepository.findActiveTimeslotsByDoctorIdWithDays(doctorId);
        
        // Group by Day (using LinkedHashMap to maintain order)
        Map<Long, DoctorAvailabilityDTO> availabilityMap = new LinkedHashMap<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        for (DoctorTimeslotEntity slot : timeslots) {
            Long dayId = slot.getDay().getId();
            
            DoctorAvailabilityDTO dayDto = availabilityMap.computeIfAbsent(dayId, k -> 
                DoctorAvailabilityDTO.builder()
                    .dayId(dayId)
                    .dayName(slot.getDay().getDayName())
                    .shortName(slot.getDay().getShortName())
                    .slots(new ArrayList<>())
                    .build()
            );

            // Generate dynamically-sized slots based on global DB setting (fallback to 60)
            int slotDurationMinutes = 60;
            List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
            if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
                slotDurationMinutes = timings.get(0).getSlotTime();
            }

            LocalTime current = slot.getStartTime();
            LocalTime end = slot.getEndTime();

            // Make sure adding the slot duration doesn't exceed the end time block
            while (current.plusMinutes(slotDurationMinutes).compareTo(end) <= 0 || current.isBefore(end)) {
                
                // If the next slot exceeds the shift end, we consider breaking or adding a truncated slot
                // To keep it simple, we just generate the slot if the 'current' time is before 'end'.
                if(current.plusMinutes(slotDurationMinutes).isAfter(end)) {
                    break;
                }

                String period = getPeriodOfDay(current);
                String formattedTime = current.format(timeFormatter);
                
                dayDto.getSlots().add(DoctorAvailabilityDTO.TimeSlotDTO.builder()
                    .time(formattedTime)
                    .period(period)
                    .build());
                
                // Advance by slot_time minutes
                current = current.plusMinutes(slotDurationMinutes);
            }
        }
        
        return new ArrayList<>(availabilityMap.values());
    }

    private String getPeriodOfDay(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 5 && hour < 12) {
            return "Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Evening";
        } else {
            return "Night";
        }
    }

    @Transactional(readOnly = true)
    public List<DoctorReviewDTO> getDoctorReviews(Long doctorId) {
        Specification<DoctorReviewEntity> spec = Specification.where(DoctorReviewSpecification.hasDoctorId(doctorId))
                .and(DoctorReviewSpecification.isActive())
                .and(DoctorReviewSpecification.withUser());
        
        List<DoctorReviewEntity> reviews = doctorReviewRepository.findAll(spec);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        return reviews.stream().map(review -> {
            String userName = "Anonymous User";
            if (review.getUser() != null) {
                userName = review.getUser().getFirstName();
                if (review.getUser().getLastName() != null) {
                    userName += " " + review.getUser().getLastName();
                }
            }
            
            String formattedDate = review.getCreatedAt() != null ? review.getCreatedAt().format(formatter) : "";

            return DoctorReviewDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .userName(userName.trim())
                .userAvatar(review.getUser() != null ? review.getUser().getAvatar() : null)
                .rating(review.getRating())
                .text(review.getReviewText())
                .date(formattedDate)
                .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public DoctorReviewDTO addDoctorReview(AddDoctorReviewDTO dto, String userName) {
        DoctorReviewEntity newReview = DoctorReviewEntity.builder()
                .doctorId(dto.getDoctorId())
                .userId(dto.getUserId())
                .rating(dto.getRating())
                .reviewText(dto.getText())
                .status(1)
                .build();
                
        DoctorReviewEntity saved = doctorReviewRepository.save(newReview);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = LocalDateTime.now().format(formatter); // Use current date for new review

        return DoctorReviewDTO.builder()
            .id(saved.getId())
            .userId(saved.getUserId())
            .userName(userName)
            .rating(saved.getRating())
            .text(saved.getReviewText())
            .date(formattedDate)
            .build();
    }

    @Transactional(readOnly = true)
    public DoctorPackageDTO getDoctorPackages(Long doctorId, Long supportId) {
        DoctorEntity doctor = doctorRepository.findActiveDoctorByIdWithDetails(doctorId);
        if (doctor == null) {
            throw new RuntimeException("Doctor not found with ID: " + doctorId);
        }

        // 1. Durations from doctor_slot_timings (status = 1)
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        List<DoctorPackageDTO.DurationDTO> durations = timings.stream()
            .filter(t -> t.getStatus() != null && t.getStatus() == 1)
            .map(t -> DoctorPackageDTO.DurationDTO.builder()
                .id(t.getId())
                .minutes(t.getSlotTime())
                .formattedLabel(t.getSlotTime() + " minutes")
                .build())
            .collect(Collectors.toList());

        // 2. Contracts from contract_type (Video/Audio)
        List<ContractTypeEntity> contractTypes = contractTypeRepository.findByStatus(1);

        // 3. Determine the base duration for pricePerMin (prioritize timings table as per request)
        int baseDuration = 60;
        if (!durations.isEmpty()) {
            baseDuration = durations.get(0).getMinutes();
        }

        // 4. Prices and standard duration from doctor_services
        DoctorServiceEntity activeService = null;
        if (doctor.getDoctorServices() != null && !doctor.getDoctorServices().isEmpty()) {
            if (supportId != null) {
                activeService = doctor.getDoctorServices().stream()
                    .filter(ds -> supportId.equals(ds.getSupportCategoryId()))
                    .findFirst().orElse(null);
            }
            
            // Fallback to first available if no match found for specific supportId
            if (activeService == null) {
                activeService = doctor.getDoctorServices().iterator().next();
            }
        }

        final DoctorServiceEntity serviceRef = activeService;
        final int finalBaseDuration = baseDuration;
        
        List<DoctorPackageDTO.PackageDTO> packages = contractTypes.stream().map(ct -> {
            Double basePrice = 0.0;
            Double perMinPrice = 0.0;
            int standardDuration = finalBaseDuration;
            String description = ct.getName() + " call with Therapist";

            if (serviceRef != null) {
                // If doctor has a specific duration, use it, otherwise use the one from timings table
                if (serviceRef.getDuration() != null && serviceRef.getDuration() > 0) {
                    standardDuration = serviceRef.getDuration();
                }
                
                if ("Video".equalsIgnoreCase(ct.getName())) {
                    basePrice = (serviceRef.getVideoCallPrice() != null) ? serviceRef.getVideoCallPrice() : 0.0;
                } else if ("Audio".equalsIgnoreCase(ct.getName()) || "Voice".equalsIgnoreCase(ct.getName())) {
                    basePrice = (serviceRef.getVoiceCallPrice() != null) ? serviceRef.getVoiceCallPrice() : 0.0;
                }
                
                if (basePrice > 0) {
                    perMinPrice = basePrice / standardDuration;
                }
            }

            return DoctorPackageDTO.PackageDTO.builder()
                .typeId(ct.getId())
                .typeName(ct.getName() + " Call")
                .description(description)
                .basePrice(basePrice)
                .pricePerMin(perMinPrice)
                .standardDuration(standardDuration)
                .build();
        }).collect(Collectors.toList());

        return DoctorPackageDTO.builder()
            .doctorId(doctorId)
            .durations(durations)
            .packages(packages)
            .build();
    }
}
