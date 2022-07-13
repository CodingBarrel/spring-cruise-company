package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.repository.RoutePointRepository;

import java.util.List;

/**
 * A service class that connects controller and model layers, thereby isolating business-logic. Controls requests
 * related to route-points.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Service
 * @see RoutePoint
 */

@Log4j2
@Service
public class RoutePointService {
    private final RoutePointRepository routePointRepository;

    @Autowired
    public RoutePointService(RoutePointRepository routePointRepository) {
        this.routePointRepository = routePointRepository;
    }

    public List<RoutePoint> findByRouteId(long id){
        return routePointRepository.findByRouteId(id);
    }

    public RoutePoint findById(long id){
        return routePointRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to find route routepoint by id: not exists [id=" + id + "]"));
    }

    public RoutePoint findByName(String name){
        return routePointRepository.findByName(name).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to find routepoint: not exists [name=" + name + "]"));
    }

    public boolean existsByName(String name) {
        return routePointRepository.existsByName(name);
    }

    public void create(RoutePoint routePoint){
        if (routePoint.getId() != null && routePointRepository.existsById(routePoint.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create routepoint: already exists [id=" + routePoint.getId() + "]");
        routePointRepository.save(routePoint);
    }

    public void update(RoutePoint routePoint){
           if (!routePointRepository.existsById(routePoint.getId()))
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update routepoint: not exists");
        routePointRepository.save(routePoint);
    }

    public void delete(long id){
        if (!routePointRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete routepoint: not exists");
        routePointRepository.deleteById(id);
    }
}
