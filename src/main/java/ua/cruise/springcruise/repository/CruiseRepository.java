package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.cruise.springcruise.entity.Cruise;

import java.util.List;
import java.util.Optional;

public interface CruiseRepository extends JpaRepository<Cruise, Long>, JpaSpecificationExecutor<Cruise> {
    List<Cruise> findByOrderByIdAsc();

    Optional<Cruise> findByName(String name);
    boolean existsByName(String name);
}