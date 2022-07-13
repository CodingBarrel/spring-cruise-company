package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.repository.LinerRepository;

import java.util.List;

/**
 * A service class that connects controller and model layers, thereby isolating business-logic. Controls requests
 * related to liner.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Service
 * @see Liner
 */


@Log4j2
@Service
public class LinerService {
    private final LinerRepository linerRepository;

    @Autowired
    public LinerService(LinerRepository linerRepository) {
        this.linerRepository = linerRepository;
    }

    public List<Liner> findAll(){
        return linerRepository.findByOrderByIdAsc();
    }

    public Liner findById(long id){
        return linerRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find liner by id: not exists [id=" + id + "]"));
    }

    public Liner findByName(String name){
        return linerRepository.findByName(name).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find liner by name: not exists [name=" + name + "]"));
    }

    public boolean existsByName(String name) {
        return linerRepository.existsByName(name);
    }

    public void update(Liner liner) throws ResponseStatusException{
        if (!linerRepository.existsById(liner.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update liner: liner not exists [id=" + liner.getId() + "]");
        linerRepository.save(liner);
    }

    public void create(Liner liner) throws ResponseStatusException{
        if (liner.getId() != null && linerRepository.existsById(liner.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create liner: liner already exists [id=" + liner.getId() + "]");
        linerRepository.save(liner);
    }

    public void delete(long id) throws ResponseStatusException{
        if (!linerRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete liner: liner not exists [id=" + id + "]");
        linerRepository.deleteById(id);
    }
}
