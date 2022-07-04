package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.repository.LinerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LinerService {
    private final LinerRepository linerRepository;

    @Autowired
    public LinerService(LinerRepository linerRepository) {
        this.linerRepository = linerRepository;
    }

    public List<Liner> findAll(){
        return linerRepository.findAll();
    }

    public Optional<Liner> findById(long id){
        return linerRepository.findById(id);
    }

    @Transactional
    public void update(Liner liner) throws ResponseStatusException{
        linerRepository.save(liner);
    }

    @Transactional
    public void create(Liner liner) throws ResponseStatusException{
        linerRepository.save(liner);
    }

    public void delete(long id) throws ResponseStatusException{
        linerRepository.deleteById(id);
    }
}
