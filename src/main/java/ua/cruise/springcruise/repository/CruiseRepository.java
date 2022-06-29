package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.Cruise;

import java.util.Optional;

public interface CruiseRepository extends JpaRepository<Cruise, Long> {
    Optional<Cruise> findByName(String name);


}