package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long> {
    List<TicketStatus> findByOrderByIdAsc();

    Optional<TicketStatus> findById(long id);
}