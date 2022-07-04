package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
}