package az.telegram.bot.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "state")
    String state;

    @Column(name = "regex")
    String regex;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    List<QuestionTranslate> questionTranslates = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "question")
    Set<Action> actions = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
