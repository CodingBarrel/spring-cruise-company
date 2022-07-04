package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;

public interface CruiseStatusRepository extends JpaRepository<CruiseStatus, Long> {
}