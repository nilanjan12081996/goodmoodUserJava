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
import java.util.List;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    private final DoctorAppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final FcmService fcmService;

    public NotificationScheduler(DoctorAppointmentRepository appointmentRepository,
                                 UserRepository userRepository,
                                 DoctorRepository doctorRepository,
                                 FcmService fcmService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.fcmService = fcmService;
    }

    // Runs every minute
    @Scheduled(cron = "0 * * * * *")
    public void scheduleAppointmentReminders() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        LocalTime in15Mins = now.plusMinutes(15);
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String nowStr = now.format(timeFormatter);
        String in15MinsStr = in15Mins.format(timeFormatter);

        // Fetch all appointments for today that are confirmed (status=1)
        List<DoctorAppointmentEntity> todaysAppointments = appointmentRepository.findByDateAndStatus(today, 1L);

        for (DoctorAppointmentEntity appointment : todaysAppointments) {
            LocalTime apptTime = appointment.getTimeonly();
            if (apptTime == null) continue;
            
            try {
                // Ignore seconds and nanos for accurate comparison
                apptTime = apptTime.withSecond(0).withNano(0);
                
                boolean isExactlyNow = apptTime.equals(now);
                boolean is15MinsAway = apptTime.equals(in15Mins);

                if (is15MinsAway) {
                    sendReminderToBoth(appointment, "Reminder: Appointment in 15 mins", 
                        "Your appointment is starting in 15 minutes at " + apptTime.format(timeFormatter));
                } else if (isExactlyNow) {
                    sendReminderToBoth(appointment, "Appointment is Starting Now!", 
                        "Your appointment scheduled for " + apptTime.format(timeFormatter) + " is starting now. Please join.");
                }
            } catch (Exception e) {
                logger.error("Error processing appointment time for reminder: " + appointment.getId(), e);
            }
        }
    }

    private void sendReminderToBoth(DoctorAppointmentEntity appointment, String title, String body) {
        doctorRepository.findById(appointment.getDoctorId()).ifPresent(doctor -> {
            if (doctor.getFcmToken() != null && !doctor.getFcmToken().trim().isEmpty()) {
                fcmService.sendPushNotification(doctor.getFcmToken(), title, body);
            }
        });

        userRepository.findById(appointment.getUserId()).ifPresent(user -> {
            if (user.getFcmToken() != null && !user.getFcmToken().trim().isEmpty()) {
                fcmService.sendPushNotification(user.getFcmToken(), title, body);
            }
        });
    }
}
