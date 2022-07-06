package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

}
