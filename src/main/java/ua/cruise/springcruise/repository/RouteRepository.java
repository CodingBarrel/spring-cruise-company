package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.Route;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByName(String name);

    boolean existsByName(String name);
}
