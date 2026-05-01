package resume.miles.doctorlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailsDTO {
    // Doctor Info
    private Long doctorId;
    private String doctorName;
    private String doctorAvatar;
    private String doctorSpecialization;
    private Integer doctorExperience;
    
    // Appointment Info
    private Long appointmentId;
    private String date; // March 23, 2025
    private String timeSlot; // 10:00 - 10:50 (50 minutes)
    private String bookingFor;
    private Long supportId;
    
    // Patient Info
    private String patientFullName; // User's full name or patient name
    private String patientGender;
    private String patientAge;
    private String patientProblem;
    
    // Package Info
    private String callType; // Voice Call / Video Call
    private String callCategory; // Support Category name
    private Double price;
    private String paymentStatus; // Paid/Unpaid
}
