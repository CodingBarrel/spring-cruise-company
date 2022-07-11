package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.cruise.springcruise.entity.RoutePoint;

import java.util.List;
import java.util.Optional;

public interface RoutePointRepository extends JpaRepository<RoutePoint, Long> {
    @Query("select r from route_point r where r.route.id = ?1")
    List<RoutePoint> findByRouteId(Long id);
    boolean existsByName(String name);
    Optional<RoutePoint> findByName(String name);
}