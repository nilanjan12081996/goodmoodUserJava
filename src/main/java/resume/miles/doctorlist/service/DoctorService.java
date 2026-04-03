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
import resume.miles.doctorlist.entity.DoctorReviewEntity;
import resume.miles.doctorlist.entity.DoctorServiceEntity;
import resume.miles.doctorlist.repository.DoctorRepository;
import resume.miles.doctorlist.repository.DoctorSlotTimingRepository;
import resume.miles.doctorlist.repository.DoctorTimeslotRepository;
import resume.miles.doctorlist.repository.DoctorReviewRepository;
import resume.miles.doctorlist.repository.ContractTypeRepository;
import resume.miles.doctorlist.entity.ContractTypeEntity;
import resume.miles.doctorlist.dto.DoctorPackageDTO;
import resume.miles.doctorlist.repository.DoctorAppointmentRepository;
import resume.miles.doctorlist.entity.DoctorAppointmentEntity;
import resume.miles.doctorlist.dto.BookAppointmentDTO;
import resume.miles.doctorlist.repository.specification.DoctorReviewSpecification;
import resume.miles.doctorlist.repository.specification.DoctorSpecification;
import org.springframework.data.jpa.domain.Specification;
import resume.miles.doctorlist.repository.AppointmentPatientRepository;
import resume.miles.doctorlist.entity.AppointmentPatientEntity;
import resume.miles.doctorlist.dto.UserAppointmentDTO;
import resume.miles.doctorlist.dto.AppointmentDetailsDTO;
import resume.miles.userregister.repository.UserRepository;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.supportcategory.repository.SupportCategoryRepository;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorTimeslotRepository doctorTimeslotRepository;
    private final DoctorSlotTimingRepository doctorSlotTimingRepository;
    private final DoctorReviewRepository doctorReviewRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final AppointmentPatientRepository appointmentPatientRepository;
    private final UserRepository userRepository;
    private final SupportCategoryRepository supportCategoryRepository;

    public DoctorService(DoctorRepository doctorRepository, 
                         DoctorTimeslotRepository doctorTimeslotRepository,
                         DoctorSlotTimingRepository doctorSlotTimingRepository,
                         DoctorReviewRepository doctorReviewRepository,
                         ContractTypeRepository contractTypeRepository,
                         DoctorAppointmentRepository doctorAppointmentRepository,
                         AppointmentPatientRepository appointmentPatientRepository,
                         UserRepository userRepository,
                         SupportCategoryRepository supportCategoryRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorTimeslotRepository = doctorTimeslotRepository;
        this.doctorSlotTimingRepository = doctorSlotTimingRepository;
        this.doctorReviewRepository = doctorReviewRepository;
        this.contractTypeRepository = contractTypeRepository;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.appointmentPatientRepository = appointmentPatientRepository;
        this.userRepository = userRepository;
        this.supportCategoryRepository = supportCategoryRepository;
    }

    @Transactional(readOnly = true)
    public List<DoctorListDTO> getAllDoctors(Long supportId) {
        Specification<DoctorEntity> spec = Specification.where(DoctorSpecification.isActiveDoctor());
        
        if (supportId != null) {
            spec = spec.and(DoctorSpecification.hasSupportId(supportId));
        }

        // Fetch all average ratings to avoid N+1 and handle different numeric types from DB
        Map<Long, Double> ratingsMap = new HashMap<>();
        doctorReviewRepository.findAllAverageRatingsGroupedByDoctor().forEach(res -> {
            if (res != null && res.length >= 2 && res[0] != null) {
                Long docId = ((Number) res[0]).longValue();
                Double avgRating = (res[1] != null) ? ((Number) res[1]).doubleValue() : null;
                ratingsMap.put(docId, avgRating);
            }
        });

        return doctorRepository.findAll(spec).stream()
            .map(doctor -> {
                DoctorServiceEntity serviceMapping = null;
                if (supportId != null) {
                    serviceMapping = doctor.getDoctorServices().stream()
                            .filter(s -> s.getSupportCategoryId().equals(supportId))
                            .findFirst()
                            .orElse(null);
                }
                
                // If supportId not provided or no mapping found for that supportId, 
                // fall back to first service mapping available
                if (serviceMapping == null && !doctor.getDoctorServices().isEmpty()) {
                    serviceMapping = doctor.getDoctorServices().iterator().next();
                }

                return DoctorListDTO.builder()
                    .id(doctor.getId())
                    .name(doctor.getFirstName() + " " + doctor.getLastName())
                    .avatar(doctor.getAvatar())
                    .experience(doctor.getDoctorAbout() != null ? doctor.getDoctorAbout().getExp() : null)
                    .languages(doctor.getDoctorAbout() != null ? doctor.getDoctorAbout().getLanguage() : null)
                    .specializations(doctor.getDoctorSpecializations().stream()
                        .map(s -> s.getSpecialization().getName())
                        .collect(Collectors.toList()))
                    .rating(ratingsMap.get(doctor.getId()))
                    .videoCallPrice(serviceMapping != null ? serviceMapping.getVideoCallPrice() : null)
                    .voiceCallPrice(serviceMapping != null ? serviceMapping.getVoiceCallPrice() : null)
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDetailsDTO getDoctorDetails(Long id) {
        DoctorEntity doctor = doctorRepository.findActiveDoctorByIdWithDetails(id);
        if (doctor == null) throw new RuntimeException("Doctor not found");

        Double avgRating = doctorReviewRepository.findAverageRatingByDoctorId(id);
        Integer reviewsCount = doctorReviewRepository.countActiveReviewsByDoctorId(id);

        DoctorServiceEntity serviceMapping = doctor.getDoctorServices().stream().findFirst().orElse(null);

        return DoctorDetailsDTO.builder()
                .id(doctor.getId())
                .name(doctor.getFirstName() + " " + doctor.getLastName())
                .avatar(doctor.getAvatar())
                .experience(doctor.getDoctorAbout() != null ? doctor.getDoctorAbout().getExp() : null)
                .languages(doctor.getDoctorAbout() != null ? doctor.getDoctorAbout().getLanguage() : null)
                .about(doctor.getDoctorAbout() != null ? doctor.getDoctorAbout().getAbout() : doctor.getEmail())
                .specializations(doctor.getDoctorSpecializations().stream()
                        .map(s -> s.getSpecialization().getName())
                        .collect(Collectors.toList()))
                .education(doctor.getDoctorEducations().stream()
                        .map(e -> e.getDegree() + " (" + e.getCourse() + ") from " + e.getInstitute())
                        .collect(Collectors.toList()))
                .videoCallPrice(serviceMapping != null ? serviceMapping.getVideoCallPrice() : null)
                .voiceCallPrice(serviceMapping != null ? serviceMapping.getVoiceCallPrice() : null)
                .reviews(reviewsCount)
                .rating(avgRating)
                .build();
    }

    @Transactional(readOnly = true)
    public List<DoctorAvailabilityDTO> getDoctorAvailability(Long doctorId) {
        List<DoctorTimeslotEntity> timeslots = doctorTimeslotRepository.findActiveTimeslotsByDoctorIdWithDays(doctorId);
        Map<Long, DoctorAvailabilityDTO> availabilityMap = new LinkedHashMap<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        int slotDurationMinutes = 60;
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
            slotDurationMinutes = timings.get(0).getSlotTime();
        }

        Map<Long, Set<LocalTime>> dayAddedTimes = new HashMap<>(); // To deduplicate slots for each day

        for (DoctorTimeslotEntity slot : timeslots) {
            Long dayId = slot.getDay().getId();
            DoctorAvailabilityDTO dto = availabilityMap.computeIfAbsent(dayId, k -> 
                DoctorAvailabilityDTO.builder()
                    .dayId(slot.getDay().getId())
                    .dayName(slot.getDay().getDayName())
                    .shortName(slot.getDay().getShortName())
                    .slots(new ArrayList<>())
                    .build()
            );

            Set<LocalTime> addedTimes = dayAddedTimes.computeIfAbsent(dayId, k -> new HashSet<>());

            LocalTime current = slot.getStartTime();
            LocalTime end = slot.getEndTime();

            while (current.plusMinutes(slotDurationMinutes).compareTo(end) <= 0) {
                if (!addedTimes.contains(current)) {
                    LocalTime slotEnd = current.plusMinutes(slotDurationMinutes);
                    dto.getSlots().add(DoctorAvailabilityDTO.TimeSlotDTO.builder()
                            .time(current.format(timeFormatter).toLowerCase())
                            .period(getPeriodOfDay(current))
                            .timeSlot(current.format(timeFormatter).toUpperCase() + " - " + slotEnd.format(timeFormatter).toUpperCase())
                            .build());
                    addedTimes.add(current);
                }
                current = current.plusMinutes(slotDurationMinutes);
            }
        }

        return new ArrayList<>(availabilityMap.values());
    }

    private String getPeriodOfDay(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 5 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 17) return "Afternoon";
        if (hour >= 17 && hour < 21) return "Evening";
        return "Night";
    }

    private LocalTime parseEndTimeFromSlot(String timeSlot, LocalTime defaultEndTime) {
        try {
            if (timeSlot == null || !timeSlot.contains("-")) return defaultEndTime;
            String endPart = timeSlot.split("-")[1].trim().toUpperCase();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
            return LocalTime.parse(endPart, formatter);
        } catch (Exception e) {
            return defaultEndTime;
        }
    }

    @Transactional
    public void addDoctorReview(AddDoctorReviewDTO reviewDto, Long userId) {
        DoctorReviewEntity review = DoctorReviewEntity.builder()
                .doctorId(reviewDto.getDoctorId())
                .userId(userId)
                .rating(reviewDto.getRating())
                .reviewText(reviewDto.getText())
                .status(1)
                .build();

        doctorReviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<DoctorReviewDTO> getDoctorReviews(Long doctorId) {
        Specification<DoctorReviewEntity> spec = Specification.where(DoctorReviewSpecification.hasDoctorId(doctorId))
                .and(DoctorReviewSpecification.isActive());
        
        return doctorReviewRepository.findAll(spec).stream()
                .map(review -> DoctorReviewDTO.builder()
                        .id(review.getId())
                        .rating(review.getRating())
                        .text(review.getReviewText())
                        .userName("User " + review.getUserId())
                        .date(review.getCreatedAt() != null ? review.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorPackageDTO getDoctorPackages(Long doctorId, Long supportId) {
        DoctorEntity doctor = doctorRepository.findActiveDoctorByIdWithDetails(doctorId);
        if (doctor == null) throw new RuntimeException("Doctor not found");

        int standardSlotMinutes = 60;
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
            standardSlotMinutes = timings.get(0).getSlotTime();
        }

        List<DoctorPackageDTO.DurationDTO> durations = new ArrayList<>();
        durations.add(DoctorPackageDTO.DurationDTO.builder()
                .id(1L)
                .minutes(standardSlotMinutes)
                .formattedLabel(standardSlotMinutes + " minutes")
                .build());

        DoctorServiceEntity serviceMapping = null;
        if (supportId != null) {
            serviceMapping = doctor.getDoctorServices().stream()
                    .filter(s -> s.getSupportCategoryId().equals(supportId))
                    .findFirst()
                    .orElse(null);
        }

        if (serviceMapping == null && !doctor.getDoctorServices().isEmpty()) {
            serviceMapping = doctor.getDoctorServices().iterator().next();
        }

        double videoPrice = 0;
        double voicePrice = 0;
        int duration = standardSlotMinutes;

        if (serviceMapping != null) {
            videoPrice = serviceMapping.getVideoCallPrice();
            voicePrice = serviceMapping.getVoiceCallPrice();
            if (serviceMapping.getDuration() != null && serviceMapping.getDuration() > 0) {
                duration = serviceMapping.getDuration();
                durations.clear();
                durations.add(DoctorPackageDTO.DurationDTO.builder()
                        .id(1L)
                        .minutes(duration)
                        .formattedLabel(duration + " minutes")
                        .build());
            }
        }

        List<DoctorPackageDTO.PackageDTO> packages = new ArrayList<>();
        List<ContractTypeEntity> contractTypes = contractTypeRepository.findByStatus(1);
        
        for (ContractTypeEntity type : contractTypes) {
            double basePrice = type.getName().toLowerCase().contains("video") ? videoPrice : voicePrice;
            packages.add(DoctorPackageDTO.PackageDTO.builder()
                .typeId(type.getId())
                .typeName(type.getName())
                .description(type.getName() + " with Therapist")
                .basePrice(basePrice)
                .pricePerMin(duration > 0 ? basePrice / duration : 0)
                .standardDuration(duration)
                .build());
        }

        return DoctorPackageDTO.builder()
            .doctorId(doctorId)
            .durations(durations)
            .packages(packages)
            .build();
    }

    @Transactional
    public Map<String, Object> bookAppointment(BookAppointmentDTO dto) {
        int slotDuration = 60;
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
            slotDuration = timings.get(0).getSlotTime();
        }

        LocalTime startTime = dto.getTimeonly();
        LocalTime endTime = parseEndTimeFromSlot(dto.getTimeSlot(), startTime.plusMinutes(slotDuration));

        // Validate that the requested slot is actually within the doctor's active schedule
        String dayName = dto.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        List<DoctorTimeslotEntity> schedule = doctorTimeslotRepository.findActiveTimeslotsByDoctorIdAndDayName(dto.getDoctorId(), dayName);
        
        boolean isValidSlot = false;
        final int finalSlotDuration = slotDuration;
        for (DoctorTimeslotEntity slot : schedule) {
            LocalTime current = slot.getStartTime();
            while (!current.plusMinutes(finalSlotDuration).isAfter(slot.getEndTime())) {
                if (current.equals(startTime)) {
                    isValidSlot = true;
                    break;
                }
                current = current.plusMinutes(finalSlotDuration);
            }
            if (isValidSlot) break;
        }

        if (!isValidSlot) {
            return getAvailabilityResponse(dto, "This doctor is not available at the requested time.");
        }

        // Check for doctor overlap
        List<DoctorAppointmentEntity> doctorBooked = doctorAppointmentRepository.findByDoctorIdAndDateAndStatus(dto.getDoctorId(), dto.getDate(), 1L);
        for (DoctorAppointmentEntity appt : doctorBooked) {
            LocalTime apptStart = appt.getTimeonly();
            LocalTime apptEnd = parseEndTimeFromSlot(appt.getTimeSlot(), apptStart.plusMinutes(slotDuration));
            if (startTime.isBefore(apptEnd) && endTime.isAfter(apptStart)) {
                return getAvailabilityResponse(dto, "The doctor is already booked for this slot.");
            }
        }

        // Check for user overlap
        List<DoctorAppointmentEntity> userBooked = doctorAppointmentRepository.findByUserIdAndDateAndStatus(dto.getUserId(), dto.getDate(), 1L);
        for (DoctorAppointmentEntity appt : userBooked) {
            LocalTime apptStart = appt.getTimeonly();
            LocalTime apptEnd = parseEndTimeFromSlot(appt.getTimeSlot(), apptStart.plusMinutes(slotDuration));
            if (startTime.isBefore(apptEnd) && endTime.isAfter(apptStart)) {
                return getAvailabilityResponse(dto, "You already have another appointment at this same time.");
            }
        }

        DoctorAppointmentEntity appointment = DoctorAppointmentEntity.builder()
                .doctorId(dto.getDoctorId())
                .userId(dto.getUserId())
                .date(dto.getDate())
                .timeSlot(dto.getTimeSlot())
                .timeonly(dto.getTimeonly())
                .calltype(dto.getCalltype())
                .status(1L)
                .reserve(0L)
                .build();
        
        appointment = doctorAppointmentRepository.save(appointment);
        
        // Save patient details
        AppointmentPatientEntity patient = AppointmentPatientEntity.builder()
                .appointmentId(appointment.getId())
                .bookingFor(dto.getBookingFor())
                .gender(dto.getGender())
                .age(dto.getAge())
                .problem(dto.getProblem())
                .build();
        appointmentPatientRepository.save(patient);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Appointment booked successfully");
        return response;
    }

    @Transactional(readOnly = true)
    public List<UserAppointmentDTO> getUserAppointments(Long userId, boolean upcoming) {
        LocalDate today = LocalDate.now();
        List<DoctorAppointmentEntity> appointments;
        
        if (upcoming) {
            // Upcoming: Date is today or later AND not complete
            appointments = doctorAppointmentRepository.findByUserIdAndDateGreaterThanEqualAndIsCompleteAndStatusOrderByDateAscTimeonlyAsc(userId, today, 0, 1L);
        } else {
            // Completed: Either explicitly marked as complete OR the date is in the past
            // Use a Set to handle potential duplicates between marked complete and past date
            Set<DoctorAppointmentEntity> completeSet = new LinkedHashSet<>();
            completeSet.addAll(doctorAppointmentRepository.findByUserIdAndIsCompleteAndStatusOrderByDateDescTimeonlyDesc(userId, 1, 1L));
            completeSet.addAll(doctorAppointmentRepository.findByUserIdAndDateLessThanAndIsCompleteAndStatusOrderByDateDescTimeonlyDesc(userId, today, 0, 1L));
            
            appointments = new ArrayList<>(completeSet);
            // Sort merged list (descending date and time)
            appointments.sort((a1, a2) -> {
                int dateComp = a2.getDate().compareTo(a1.getDate());
                if (dateComp != 0) return dateComp;
                return a2.getTimeonly().compareTo(a1.getTimeonly());
            });
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.ENGLISH);

        return appointments.stream().map(appt -> {
            DoctorEntity doctor = doctorRepository.findById(appt.getDoctorId()).orElse(null);
            String docName = doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "Unknown Doctor";
            String docAvatar = doctor != null ? doctor.getAvatar() : null;
            String spec = (doctor != null && doctor.getDoctorSpecializations() != null && !doctor.getDoctorSpecializations().isEmpty()) 
                ? doctor.getDoctorSpecializations().iterator().next().getSpecialization().getName() 
                : "Therapist";

            return UserAppointmentDTO.builder()
                .id(appt.getId())
                .doctorId(appt.getDoctorId())
                .doctorName(docName)
                .doctorAvatar(docAvatar)
                .specialization(spec)
                .date(appt.getDate().format(dateFormatter))
                .timeSlot(appt.getTimeSlot())
                .callType(appt.getCalltype())
                .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDetailsDTO getAppointmentDetails(Long appointmentId) {
        DoctorAppointmentEntity appt = doctorAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        DoctorEntity doctor = doctorRepository.findActiveDoctorByIdWithDetails(appt.getDoctorId());
        if (doctor == null) throw new RuntimeException("Doctor not found");
        
        AppointmentPatientEntity patientInfo = appointmentPatientRepository.findByAppointmentId(appointmentId)
                .orElse(new AppointmentPatientEntity());
        
        UserEntity user = userRepository.findById(appt.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        String patientFullName = "";
        if ("Self".equalsIgnoreCase(patientInfo.getBookingFor())) {
            patientFullName = (user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : "");
        } else {
            // For now, if someone else, use the user's name as we don't have a patient name field yet.
            patientFullName = (user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : "");
        }

        // Format Date: March 23, 2025
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = (appt.getDate() != null) ? appt.getDate().format(dateFormatter) : "";
        
        // Format Time: 10:00 - 10:50 (50 minutes)
        int duration = 60;
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
            duration = timings.get(0).getSlotTime();
        }
        
        LocalTime startTime = appt.getTimeonly();
        LocalTime endTime = parseEndTimeFromSlot(appt.getTimeSlot(), startTime.plusMinutes(duration));
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter) + " (" + duration + " minutes)";
        
        // Doctor details
        String specialization = doctor.getDoctorSpecializations().stream()
                .findFirst()
                .map(s -> s.getSpecialization().getName())
                .orElse("Therapist");
                
        Integer exp = (doctor.getDoctorAbout() != null) ? doctor.getDoctorAbout().getExp() : 0;
        
        // Package Info
        DoctorServiceEntity service = doctor.getDoctorServices().stream().findFirst().orElse(null);
        String callCategory = "Consultation";
        Double price = 0.0;
        if (service != null) {
            SupportCategoryEntity cat = supportCategoryRepository.findById(service.getSupportCategoryId()).orElse(null);
            if (cat != null) callCategory = cat.getName();
            price = (appt.getCalltype() != null && appt.getCalltype() == 1) ? service.getVideoCallPrice() : service.getVoiceCallPrice();
        }

        return AppointmentDetailsDTO.builder()
                .doctorId(doctor.getId())
                .doctorName(doctor.getFirstName() + " " + doctor.getLastName())
                .doctorAvatar(doctor.getAvatar())
                .doctorSpecialization(specialization)
                .doctorExperience(exp)
                .appointmentId(appt.getId())
                .date(formattedDate)
                .timeSlot(formattedTime)
                .bookingFor(patientInfo.getBookingFor())
                .patientFullName(patientFullName.trim())
                .patientGender(patientInfo.getGender())
                .patientAge(patientInfo.getAge() != null ? patientInfo.getAge() + " Years" : "")
                .patientProblem(patientInfo.getProblem())
                .callType(appt.getCalltype() != null && appt.getCalltype() == 1 ? "Video Call" : "Voice Call")
                .callCategory(callCategory)
                .price(price)
                .paymentStatus(appt.getStatus() == 1 ? "Paid" : "Unpaid")
                .build();
    }

    @Transactional
    public void completeAppointment(Long appointmentId) {
        DoctorAppointmentEntity appointment = doctorAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));
        appointment.setIsComplete(1);
        doctorAppointmentRepository.save(appointment);
    }

    private Map<String, Object> getAvailabilityResponse(BookAppointmentDTO dto, String message) {
        List<DoctorAvailabilityDTO.TimeSlotDTO> availableSlots = findAvailableSlotsForDate(dto.getDoctorId(), dto.getDate());
        String resultMessage = message;
        LocalDate searchDate = dto.getDate();

        if (availableSlots.isEmpty()) {
            for (int i = 1; i <= 7; i++) {
                searchDate = dto.getDate().plusDays(i);
                availableSlots = findAvailableSlotsForDate(dto.getDoctorId(), searchDate);
                if (!availableSlots.isEmpty()) {
                    resultMessage += " No slots available for " + dto.getDate() + ". Next available slots for " + searchDate;
                    break;
                }
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", false);
        response.put("message", resultMessage);
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("availableSlots", availableSlots);
        data.put("availableDate", searchDate);
        response.put("data", data);

        return response;
    }

    private List<DoctorAvailabilityDTO.TimeSlotDTO> findAvailableSlotsForDate(Long doctorId, LocalDate date) {
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        List<DoctorTimeslotEntity> schedule = doctorTimeslotRepository.findActiveTimeslotsByDoctorIdAndDayName(doctorId, dayName);
        
        if (schedule.isEmpty()) return new ArrayList<>();

        List<DoctorAppointmentEntity> booked = doctorAppointmentRepository.findByDoctorIdAndDateAndStatus(doctorId, date, 1L);
        List<LocalTime> bookedTimes = booked.stream().map(DoctorAppointmentEntity::getTimeonly).collect(Collectors.toList());

        Set<LocalTime> slotTimes = new LinkedHashSet<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        int slotDurationMinutes = 60;
        List<DoctorSlotTimingEntity> timings = doctorSlotTimingRepository.findAll();
        if (!timings.isEmpty() && timings.get(0).getSlotTime() != null) {
            slotDurationMinutes = timings.get(0).getSlotTime();
        }
        final int finalSlotDuration = slotDurationMinutes;

        for (DoctorTimeslotEntity slot : schedule) {
            LocalTime current = slot.getStartTime();
            LocalTime end = slot.getEndTime();

            while (current.plusMinutes(finalSlotDuration).compareTo(end) <= 0) {
                LocalTime nextStartTime = current.plusMinutes(finalSlotDuration);
                boolean overlapped = false;
                for (DoctorAppointmentEntity appt : booked) {
                    LocalTime apptStart = appt.getTimeonly();
                    LocalTime apptEnd = parseEndTimeFromSlot(appt.getTimeSlot(), apptStart.plusMinutes(finalSlotDuration));
                    if (current.isBefore(apptEnd) && nextStartTime.isAfter(apptStart)) {
                        overlapped = true;
                        break;
                    }
                }
                
                if (!overlapped) {
                    slotTimes.add(current);
                }
                current = nextStartTime;
            }
        }

        return slotTimes.stream()
            .map(t -> {
                LocalTime stEnd = t.plusMinutes(finalSlotDuration);
                return DoctorAvailabilityDTO.TimeSlotDTO.builder()
                    .time(t.format(timeFormatter).toLowerCase())
                    .period(getPeriodOfDay(t))
                    .timeSlot(t.format(timeFormatter).toUpperCase() + " - " + stEnd.format(timeFormatter).toUpperCase())
                    .build();
            })
            .collect(Collectors.toList());
    }
}
