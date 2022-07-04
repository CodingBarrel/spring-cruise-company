package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.repository.RouteRepository;

import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> findAll(){
        return routeRepository.findAll();
    }

    public Route findById(long id){
        return routeRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route not found"));
    }

    @Transactional
    public void update(Route route){
        routeRepository.save(route);
    }

    @Transactional
    public void create(Route route){
        routeRepository.save(route);
    }

    public void delete(long id){
        routeRepository.deleteById(id);
    }
}
