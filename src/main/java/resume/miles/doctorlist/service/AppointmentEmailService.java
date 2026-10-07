package resume.miles.doctorlist.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import resume.miles.doctorlist.entity.AppointmentPatientEntity;
import resume.miles.doctorlist.entity.DoctorAppointmentEntity;
import resume.miles.doctorlist.entity.DoctorEntity;
import resume.miles.doctorlist.repository.AppointmentPatientRepository;
import resume.miles.doctorlist.repository.DoctorAppointmentRepository;
import resume.miles.doctorlist.repository.DoctorRepository;
import resume.miles.userregister.entity.UserEntity;
import resume.miles.userregister.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentEmailService {

    private final JavaMailSender mailSender;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentPatientRepository appointmentPatientRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final java.util.Set<Long> sentAppointmentIds = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());

    /**
     * Sends professional confirmation emails asynchronously to both Patient and Doctor.
     */
    public void sendAppointmentConfirmationEmailsAsync(Long appointmentId) {
        if (appointmentId == null) return;
        if (!sentAppointmentIds.add(appointmentId)) {
            log.info("[AppointmentEmailService] Emails already queued/sent for appointmentId: {}", appointmentId);
            return;
        }
        CompletableFuture.runAsync(() -> {
            try {
                sendEmails(appointmentId);
            } catch (Exception e) {
                sentAppointmentIds.remove(appointmentId);
                log.error("[AppointmentEmailService] Failed to send appointment emails for appointmentId={}: {}", 
                        appointmentId, e.getMessage(), e);
            }
        });
    }

    private void sendEmails(Long appointmentId) {
        DoctorAppointmentEntity appointment = doctorAppointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            log.warn("[AppointmentEmailService] Appointment not found with ID: {}", appointmentId);
            return;
        }

        DoctorEntity doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        UserEntity user = userRepository.findById(appointment.getUserId()).orElse(null);
        AppointmentPatientEntity patient = appointmentPatientRepository.findByAppointmentId(appointmentId).orElse(null);

        String doctorName = doctor != null ? "Dr. " + doctor.getFirstName() + " " + doctor.getLastName() : "Doctor";
        String doctorSpecialization = "Therapist & Wellness Consultant";
        if (doctor != null && doctor.getDoctorSpecializations() != null && !doctor.getDoctorSpecializations().isEmpty()) {
            doctorSpecialization = doctor.getDoctorSpecializations().iterator().next().getSpecialization().getName();
        }

        String userName = user != null ? (user.getFirstName() != null ? user.getFirstName() + " " + (user.getLastName() != null ? user.getLastName() : "") : "Valued Member") : "Valued Member";
        String patientName = (patient != null && patient.getBookingFor() != null && !patient.getBookingFor().trim().isEmpty())
                ? patient.getBookingFor()
                : userName;

        String formattedDate = appointment.getDate() != null 
                ? appointment.getDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH))
                : "Scheduled Date";
        
        String timeSlot = appointment.getTimeSlot() != null ? appointment.getTimeSlot() : "Scheduled Time";
        String consultationType = (appointment.getCalltype() != null && appointment.getCalltype() == 2) 
                ? "Voice Call Consultation" 
                : "Video Call Consultation";

        String patientAge = (patient != null && patient.getAge() != null) ? patient.getAge() : "N/A";
        String patientGender = (patient != null && patient.getGender() != null) ? patient.getGender() : "N/A";
        String patientProblem = (patient != null && patient.getProblem() != null && !patient.getProblem().trim().isEmpty()) 
                ? patient.getProblem() 
                : "General Wellness & Consultation";

        // 1. Send Email to Patient / User
        if (user != null && user.getEmail() != null && user.getEmail().contains("@")) {
            try {
                String patientHtml = buildPatientEmailHtml(
                        userName,
                        doctorName,
                        doctorSpecialization,
                        formattedDate,
                        timeSlot,
                        consultationType,
                        patientName,
                        appointmentId
                );
                sendHtmlEmail(
                        user.getEmail(),
                        "Appointment Confirmed: " + doctorName + " - Good Mood Solutions",
                        patientHtml
                );
                log.info("[AppointmentEmailService] Patient confirmation email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("[AppointmentEmailService] Error sending email to patient {}: {}", user.getEmail(), e.getMessage());
            }
        } else {
            log.warn("[AppointmentEmailService] No valid email found for user ID: {}", appointment.getUserId());
        }

        // 2. Send Email to Doctor
        if (doctor != null && doctor.getEmail() != null && doctor.getEmail().contains("@")) {
            try {
                String doctorHtml = buildDoctorEmailHtml(
                        doctorName,
                        patientName,
                        patientAge,
                        patientGender,
                        patientProblem,
                        formattedDate,
                        timeSlot,
                        consultationType,
                        appointmentId
                );
                sendHtmlEmail(
                        doctor.getEmail(),
                        "New Appointment Scheduled: " + patientName + " - Good Mood Solutions",
                        doctorHtml
                );
                log.info("[AppointmentEmailService] Doctor notification email sent to: {}", doctor.getEmail());
            } catch (Exception e) {
                log.error("[AppointmentEmailService] Error sending email to doctor {}: {}", doctor.getEmail(), e.getMessage());
            }
        } else {
            log.warn("[AppointmentEmailService] No valid email found for doctor ID: {}", appointment.getDoctorId());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Good Mood Solutions <" + senderEmail + ">");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

    private String buildPatientEmailHtml(
            String userName,
            String doctorName,
            String specialization,
            String date,
            String timeSlot,
            String mode,
            String patientName,
            Long appointmentId
    ) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>Appointment Confirmation</title>
        </head>
        <body style="margin: 0; padding: 0; background-color: #F1F5F9; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #1E293B;">
          <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #F1F5F9; padding: 30px 10px;">
            <tr>
              <td align="center">
                <table role="presentation" width="100%%" style="max-width: 600px; background-color: #FFFFFF; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.06); border: 1px solid #E2E8F0;" cellspacing="0" cellpadding="0">
                  
                  <!-- Top Banner Header -->
                  <tr>
                    <td style="background: linear-gradient(135deg, #0284C7 0%%, #0EA5E9 50%%, #38BDF8 100%%); padding: 36px 30px; text-align: center;">
                      <div style="font-size: 13px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase; color: #E0F2FE; margin-bottom: 6px;">
                        GOOD MOOD SOLUTIONS
                      </div>
                      <h1 style="margin: 0; font-size: 26px; font-weight: 800; color: #FFFFFF; letter-spacing: -0.5px;">
                        Appointment Confirmed!
                      </h1>
                      <div style="margin-top: 12px; display: inline-block; background-color: rgba(255, 255, 255, 0.2); padding: 6px 14px; border-radius: 20px; color: #FFFFFF; font-size: 12px; font-weight: 600;">
                        Booking ID #%d
                      </div>
                    </td>
                  </tr>

                  <!-- Main Content Area -->
                  <tr>
                    <td style="padding: 32px 30px;">
                      <p style="margin: 0 0 16px 0; font-size: 16px; line-height: 1.6; color: #334155;">
                        Dear <strong>%s</strong>,
                      </p>
                      <p style="margin: 0 0 24px 0; font-size: 15px; line-height: 1.6; color: #475569;">
                        Your appointment has been successfully scheduled. We are delighted to assist you on your journey to better wellness. Please review your session details below:
                      </p>

                      <!-- Appointment Details Card -->
                      <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #F8FAFC; border-radius: 12px; border: 1px solid #E2E8F0; margin-bottom: 24px;">
                        <tr>
                          <td style="padding: 20px;">
                            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0">
                              
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; width: 38%%; font-weight: 600;">👨‍⚕️ Specialist:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 700;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">🩺 Specialization:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0369A1; font-weight: 600;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">📅 Date:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">⏰ Time Slot:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 700;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">⏳ Duration:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #16A34A; font-weight: 700;">45 Minutes Session</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">📹 Mode:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">👤 Patient Name:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">%s</td>
                              </tr>

                            </table>
                          </td>
                        </tr>
                      </table>

                      <!-- Instructions Box -->
                      <div style="background-color: #EFF6FF; border-left: 4px solid #0284C7; padding: 16px; border-radius: 8px; margin-bottom: 24px;">
                        <h4 style="margin: 0 0 8px 0; font-size: 14px; color: #0369A1; font-weight: 700;">
                          💡 Important Guidelines for Your Call:
                        </h4>
                        <ul style="margin: 0; padding-left: 20px; font-size: 13px; color: #334155; line-height: 1.6;">
                          <li>Please open the <strong>Good Mood App</strong> 5 minutes prior to the scheduled start time.</li>
                          <li>Go to <strong>My Appointments</strong> and click <strong>Join Call</strong> when ready.</li>
                          <li>The main consultation is <strong>45 minutes</strong> with a 10-minute grace window for smooth wrap-up.</li>
                          <li>Ensure you have a quiet environment and stable internet connection.</li>
                        </ul>
                      </div>

                      <p style="margin: 0 0 8px 0; font-size: 14px; line-height: 1.5; color: #475569;">
                        If you have any queries or need assistance, feel free to contact us via the app support section.
                      </p>
                      
                      <p style="margin: 20px 0 0 0; font-size: 14px; color: #1E293B;">
                        Thank you for choosing <strong>Good Mood Solutions</strong>!<br>
                        <span style="color: #64748B; font-size: 13px;">Your Mental Health & Wellbeing Partner</span>
                      </p>
                    </td>
                  </tr>

                  <!-- Footer -->
                  <tr>
                    <td style="background-color: #F8FAFC; padding: 20px 30px; text-align: center; border-top: 1px solid #E2E8F0; font-size: 12px; color: #94A3B8;">
                      © 2026 Good Mood Solutions. All rights reserved.<br>
                      This is an automated appointment confirmation email. Please do not reply directly to this email.
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(
                appointmentId,
                userName,
                doctorName,
                specialization,
                date,
                timeSlot,
                mode,
                patientName
        );
    }

    private String buildDoctorEmailHtml(
            String doctorName,
            String patientName,
            String age,
            String gender,
            String problem,
            String date,
            String timeSlot,
            String mode,
            Long appointmentId
    ) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <title>New Appointment Notification</title>
        </head>
        <body style="margin: 0; padding: 0; background-color: #F1F5F9; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; color: #1E293B;">
          <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #F1F5F9; padding: 30px 10px;">
            <tr>
              <td align="center">
                <table role="presentation" width="100%%" style="max-width: 600px; background-color: #FFFFFF; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.06); border: 1px solid #E2E8F0;" cellspacing="0" cellpadding="0">
                  
                  <!-- Top Banner Header -->
                  <tr>
                    <td style="background: linear-gradient(135deg, #0F766E 0%%, #0D9488 50%%, #14B8A6 100%%); padding: 36px 30px; text-align: center;">
                      <div style="font-size: 13px; font-weight: 700; letter-spacing: 2px; text-transform: uppercase; color: #CCFBF1; margin-bottom: 6px;">
                        GOOD MOOD DOCTORS PORTAL
                      </div>
                      <h1 style="margin: 0; font-size: 26px; font-weight: 800; color: #FFFFFF; letter-spacing: -0.5px;">
                        New Appointment Scheduled
                      </h1>
                      <div style="margin-top: 12px; display: inline-block; background-color: rgba(255, 255, 255, 0.2); padding: 6px 14px; border-radius: 20px; color: #FFFFFF; font-size: 12px; font-weight: 600;">
                        Appointment ID #%d
                      </div>
                    </td>
                  </tr>

                  <!-- Main Content Area -->
                  <tr>
                    <td style="padding: 32px 30px;">
                      <p style="margin: 0 0 16px 0; font-size: 16px; line-height: 1.6; color: #334155;">
                        Dear <strong>%s</strong>,
                      </p>
                      <p style="margin: 0 0 24px 0; font-size: 15px; line-height: 1.6; color: #475569;">
                        A new consultation appointment has been booked with you by a patient. Please find the patient and appointment details below:
                      </p>

                      <!-- Appointment Details Card -->
                      <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color: #F8FAFC; border-radius: 12px; border: 1px solid #E2E8F0; margin-bottom: 24px;">
                        <tr>
                          <td style="padding: 20px;">
                            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0">
                              
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; width: 38%%; font-weight: 600;">👤 Patient Name:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 700;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">📋 Demographics:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">Age: %s | Gender: %s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">📅 Date:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">⏰ Time Slot:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 700;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">⏳ Duration:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F766E; font-weight: 700;">45 Minutes (Max 55 Mins with Grace)</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">📹 Mode:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #0F172A; font-weight: 600;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 8px 0; font-size: 13px; color: #64748B; font-weight: 600;">💬 Reported Concern:</td>
                                <td style="padding: 8px 0; font-size: 14px; color: #334155; font-weight: 500; font-style: italic;">%s</td>
                              </tr>

                            </table>
                          </td>
                        </tr>
                      </table>

                      <!-- Instructions Box -->
                      <div style="background-color: #F0FDFA; border-left: 4px solid #0D9488; padding: 16px; border-radius: 8px; margin-bottom: 24px;">
                        <h4 style="margin: 0 0 8px 0; font-size: 14px; color: #0F766E; font-weight: 700;">
                          💡 Clinical Guidelines:
                        </h4>
                        <ul style="margin: 0; padding-left: 20px; font-size: 13px; color: #334155; line-height: 1.6;">
                          <li>Please join on time via the <strong>Good Mood Doctors App</strong> under <strong>My Appointments</strong>.</li>
                          <li>The official session countdown is <strong>45 minutes</strong> aligned with the scheduled slot.</li>
                          <li>An automatic <strong>10-minute grace period</strong> is enabled for wrapping up notes before auto-cut at 55 minutes.</li>
                          <li>Medical history and previous notes can be reviewed inside the app.</li>
                        </ul>
                      </div>

                      <p style="margin: 20px 0 0 0; font-size: 14px; color: #1E293B;">
                        Thank you for your dedicated care and support!<br>
                        <span style="color: #64748B; font-size: 13px;">Good Mood Solutions Team</span>
                      </p>
                    </td>
                  </tr>

                  <!-- Footer -->
                  <tr>
                    <td style="background-color: #F8FAFC; padding: 20px 30px; text-align: center; border-top: 1px solid #E2E8F0; font-size: 12px; color: #94A3B8;">
                      © 2026 Good Mood Solutions. All rights reserved.<br>
                      Confidential Medical Appointment Notification. Please do not reply directly to this email.
                    </td>
                  </tr>

                </table>
              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(
                appointmentId,
                doctorName,
                patientName,
                age,
                gender,
                date,
                timeSlot,
                mode,
                problem
        );
    }
}
