package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.CruiseStatusDict;

public interface CruiseStatusDictRepository extends JpaRepository<CruiseStatusDict, Long> {
}