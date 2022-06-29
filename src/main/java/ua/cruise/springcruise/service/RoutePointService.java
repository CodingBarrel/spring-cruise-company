package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.repository.RoutePointRepository;

import java.util.List;

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

    @Transactional
    public void create(RoutePoint routePoint){
        routePointRepository.save(routePoint);
    }

    @Transactional
    public void update(RoutePoint routePoint){
           RoutePoint foundRoutePoint = routePointRepository.findById(routePoint.getId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
           routePoint.setRoute(foundRoutePoint.getRoute());
           routePointRepository.save(routePoint);
    }

    public void delete(long id){
        routePointRepository.deleteById(id);
    }
}
