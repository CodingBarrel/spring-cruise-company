package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.Liner;

import java.util.Optional;

public interface LinerRepository extends JpaRepository<Liner, Long> {
    boolean existsByName(String name);
    Optional<Liner> findByName(String name);
}