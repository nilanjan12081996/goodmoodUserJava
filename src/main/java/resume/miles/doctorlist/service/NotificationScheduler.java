package resume.miles.doctorlist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import resume.miles.config.firebase.FcmService;
import resume.miles.doctorlist.entity.DoctorAppointmentEntity;
import resume.miles.doctorlist.repository.DoctorAppointmentRepository;
import resume.miles.userregister.repository.UserRepository;
import resume.miles.doctorlist.repository.DoctorRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    private final DoctorAppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final FcmService fcmService;

    // Track sent reminders to prevent duplicates
    private final Set<Long> sent15MinsReminders = ConcurrentHashMap.newKeySet();
    private final Set<Long> sentRealtimeReminders = ConcurrentHashMap.newKeySet();
    private LocalDate lastTrackedDate = LocalDate.now();

    public NotificationScheduler(DoctorAppointmentRepository appointmentRepository,
                                 UserRepository userRepository,
                                 DoctorRepository doctorRepository,
                                 FcmService fcmService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.fcmService = fcmService;
    }

    // Runs every minute to check and trigger 15-min and real-time reminders
    @Scheduled(cron = "0 * * * * *")
    public void scheduleAppointmentReminders() {
        LocalDate today = LocalDate.now();
        
        // Reset tracked sets on day change
        if (!today.equals(lastTrackedDate)) {
            sent15MinsReminders.clear();
            sentRealtimeReminders.clear();
            lastTrackedDate = today;
        }

        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        // Fetch all appointments for today that are confirmed (status=1)
        List<DoctorAppointmentEntity> todaysAppointments = appointmentRepository.findByDateAndStatus(today, 1L);

        for (DoctorAppointmentEntity appointment : todaysAppointments) {
            LocalTime apptTime = appointment.getTimeonly();
            if (apptTime == null) continue;
            
            try {
                apptTime = apptTime.withSecond(0).withNano(0);
                long minutesUntil = ChronoUnit.MINUTES.between(now, apptTime);
                String formattedTime = apptTime.format(timeFormatter);

                // 2. 15 Minutes Before Reminder ("tarpor 15 min")
                if (minutesUntil >= 14 && minutesUntil <= 15 && !sent15MinsReminders.contains(appointment.getId())) {
                    sent15MinsReminders.add(appointment.getId());
                    logger.info("Sending 15-min reminder for appointment ID {}", appointment.getId());

                    sendReminder(
                        appointment,
                        "Appointment in 15 Minutes",
                        "Your consultation starts in 15 minutes at " + formattedTime + ". Please get ready.",
                        "Appointment in 15 Minutes",
                        "Your session with the doctor starts in 15 minutes at " + formattedTime + ". Please get ready."
                    );
                }
                // 3. Realtime Reminder ("taepor realtime - starting now")
                else if (minutesUntil >= -1 && minutesUntil <= 0 && !sentRealtimeReminders.contains(appointment.getId())) {
                    sentRealtimeReminders.add(appointment.getId());
                    logger.info("Sending real-time starting now notification for appointment ID {}", appointment.getId());

                    sendReminder(
                        appointment,
                        "Appointment Starting Now",
                        "Your consultation scheduled for " + formattedTime + " is starting now. Please join.",
                        "Appointment Starting Now",
                        "Your consultation with the doctor is starting now. Please join your session."
                    );
                }
            } catch (Exception e) {
                logger.error("Error processing appointment time for reminder: " + appointment.getId(), e);
            }
        }
    }

    private void sendReminder(DoctorAppointmentEntity appointment, 
                              String doctorTitle, String doctorBody,
                              String userTitle, String userBody) {
        doctorRepository.findById(appointment.getDoctorId()).ifPresent(doctor -> {
            if (doctor.getFcmToken() != null && !doctor.getFcmToken().trim().isEmpty()) {
                fcmService.sendPushNotification(doctor.getFcmToken(), doctorTitle, doctorBody);
            } else {
                logger.warn("Doctor {} has no FCM token for appointment {}", doctor.getId(), appointment.getId());
            }
        });

        userRepository.findById(appointment.getUserId()).ifPresent(user -> {
            if (user.getFcmToken() != null && !user.getFcmToken().trim().isEmpty()) {
                fcmService.sendPushNotification(user.getFcmToken(), userTitle, userBody);
            } else {
                logger.warn("User {} has no FCM token for appointment {}", user.getId(), appointment.getId());
            }
        });
    }
}
