package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.repository.RouteRepository;

import java.util.List;

@Log4j2
@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> findAll() {
        return routeRepository.findByOrderByIdAsc();
    }

    public boolean existsByName(String name) {
        return routeRepository.existsByName(name);
    }

    public Route findById(long id) {
        return routeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find route: not exists [id=" + id + "]"));
    }

    public Route findByName(String name){
        return routeRepository.findByName(name).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find route: not exists [name=" + name + "]"));
    }

    public void update(Route route) {
        if (!routeRepository.existsById(route.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update route: not exists [id=" + route.getId() + "]");
        routeRepository.save(route);
    }

    public void create(Route route) {
        if (route.getId() != null && routeRepository.existsById(route.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create route: already exists [id=" + route.getId() + "]");
        routeRepository.save(route);
    }

    public void delete(long id) {
        if (!routeRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete route: not exists [id=" + id + "]");
        routeRepository.deleteById(id);
    }
}
