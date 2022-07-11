package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;

import java.util.List;
import java.util.Optional;

public interface CruiseStatusRepository extends JpaRepository<CruiseStatus, Long> {
    List<CruiseStatus> findByOrderByIdAsc();

    Optional<CruiseStatus> findById(long id);
}