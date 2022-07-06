package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
    Optional<TicketStatus> findById(long id);
}