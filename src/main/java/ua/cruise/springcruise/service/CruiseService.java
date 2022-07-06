package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.dict.CruiseStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CruiseService {
    private final CruiseRepository cruiseRepository;
    private final CruiseStatusRepository statusDictRepository;

    @Autowired
    public CruiseService(CruiseRepository cruiseRepository, CruiseStatusRepository statusDictRepository) {
        this.cruiseRepository = cruiseRepository;
        this.statusDictRepository = statusDictRepository;
    }

    public List<Cruise> findAll(){
        return cruiseRepository.findAll();
    }

    public Optional<Cruise> findById(long id){
        return cruiseRepository.findById(id);
    }

    @Transactional
    public void update(Cruise cruise){
        cruiseRepository.save(cruise);
    }

    @Transactional
    public void create(Cruise cruise){
        cruiseRepository.save(cruise);
    }

    public List<CruiseStatus> findAllStatuses(){
        return statusDictRepository.findAll();
    }

    public CruiseStatus findStatusById(long id){
        return statusDictRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public void delete(long id){
        cruiseRepository.deleteById(id);
    }
}
