package resume.miles.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import resume.miles.doctorlist.entity.DoctorAppointmentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DoctorAppointmentEntity appointment;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "transaction_code", nullable = false)
    private String transactionCode;

    @Column(name = "transaction_price", nullable = false)
    private BigDecimal transactionPrice;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "transaction_status", nullable = false)
    private String transactionStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
