package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.TicketStatusDict;

public interface TicketStatusDictRepository extends JpaRepository<TicketStatusDict, Long> {
}