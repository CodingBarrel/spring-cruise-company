package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.CruiseDTO;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.dict.CruiseStatusRepository;
import ua.cruise.springcruise.util.CruiseMapper;

import java.util.List;
import java.util.Optional;

@Service
public class CruiseService {
    private final CruiseRepository cruiseRepository;
    private final CruiseMapper cruiseMapper;
    private final CruiseStatusRepository statusDictRepository;

    @Autowired
    public CruiseService(CruiseRepository cruiseRepository, CruiseMapper cruiseMapper, CruiseStatusRepository statusDictRepository) {
        this.cruiseRepository = cruiseRepository;
        this.cruiseMapper = cruiseMapper;
        this.statusDictRepository = statusDictRepository;
    }

    public List<Cruise> findAll(){
        return cruiseRepository.findAll();
    }

    public Optional<Cruise> findById(long id){
        return cruiseRepository.findById(id);
    }

    public Optional<Cruise> findByName(String name){
        return cruiseRepository.findByName(name);
    }

    @Transactional
    public void update(CruiseDTO cruiseDTO){
        Cruise cruise = cruiseMapper.DTOtoCruise(cruiseDTO);
        cruise.setStatus(statusDictRepository.findById(cruiseDTO.getStatus().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Status not found")));
        cruiseRepository.save(cruise);
    }

    @Transactional
    public void create(CruiseDTO cruiseDTO){
        Cruise cruise = cruiseMapper.DTOtoCruise(cruiseDTO);
        cruise.setStatus(statusDictRepository.findById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Status not found")));
        cruiseRepository.save(cruise);
    }

    public List<CruiseStatus> findStatusDict(){
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
