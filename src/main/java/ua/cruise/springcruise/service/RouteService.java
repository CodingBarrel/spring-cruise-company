package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.repository.RouteRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Optional<Route> findById(long id){
        return routeRepository.findById(id);
    }

    @Transactional
    public void update(Route route){
        Optional<Route> foundRoute = routeRepository.findByName(route.getName());
        if (foundRoute.isPresent()&& !Objects.equals(route.getId(), foundRoute.get().getId())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        routeRepository.save(route);
    }

    @Transactional
    public void create(Route route){
        if (routeRepository.existsByName(route.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        routeRepository.save(route);
    }

    public void delete(long id){
        if (!routeRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        routeRepository.deleteById(id);
    }
}
