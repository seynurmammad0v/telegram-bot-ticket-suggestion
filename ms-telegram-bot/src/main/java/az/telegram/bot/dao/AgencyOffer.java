package az.telegram.bot.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agency_offer")
public class AgencyOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "agency_username")
    String agencyUsername;

    @Column(name = "session_id")
    Long sessionId;

    @Column(name = "message_id")
    Integer messageId;

    @Transient
    byte[] file;

    @Column(name = "file_path")
    String filePath;

    @Column(name = "is_accepted")
    boolean isAccepted;


    @Column(name = "details_id")
    Long offerDetailsId;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

}
