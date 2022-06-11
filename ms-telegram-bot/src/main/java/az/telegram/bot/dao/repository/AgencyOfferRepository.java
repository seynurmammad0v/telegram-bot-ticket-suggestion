package az.telegram.bot.dao.repository;

import az.telegram.bot.dao.AgencyOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyOfferRepository extends JpaRepository<AgencyOffer, Long> {
    AgencyOffer getByMessageIdAndSessionId(Integer messageId, Long sessionId);

}
