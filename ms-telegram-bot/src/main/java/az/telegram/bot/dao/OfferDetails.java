package az.telegram.bot.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "offer_details")
public class OfferDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "price")
    Long price;

    @Column(columnDefinition = "TEXT", name = "description")
    String description;

    @Column(columnDefinition = "TEXT", name = "notes")
    String notes;

    @Column(name = "dateInterim")
    String dateInterim;

}