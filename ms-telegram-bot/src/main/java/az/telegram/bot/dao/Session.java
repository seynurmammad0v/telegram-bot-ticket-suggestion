package az.telegram.bot.dao;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import az.telegram.bot.model.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.telegram.telegrambots.meta.api.objects.Contact;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "chat_id")
    Long chatId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "session")
    List<Answer> answers = new ArrayList<>();

    @Column(name = "next_message_id")
    Integer nextMessageId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

//    Contact contact;

    @Column(name = "lock")
    Boolean lock = false;

    @Column(name = "lang_id")
    Long langId = 4L;

    @Column(name = "offers_count")
    Integer offersCount = 0;

    @Column(name = "sent_count")
    Integer sentCount = 0;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "expired_at")
    LocalDateTime expiredAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        lock = false;
        langId = 4L;
        offersCount = 0;
        sentCount = 0;
    }

}
