package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.dict.CruiseStatusRepository;
import ua.cruise.springcruise.util.Constants;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CruiseService {
    private final CruiseRepository cruiseRepository;
    private final CruiseStatusRepository statusDictRepository;

    @Autowired
    public CruiseService(CruiseRepository cruiseRepository, CruiseStatusRepository statusDictRepository) {
        this.cruiseRepository = cruiseRepository;
        this.statusDictRepository = statusDictRepository;
    }

    public List<Cruise> findAll() {
        return cruiseRepository.findAll();
    }

    public Page<Cruise> findByStartDateTimeAndDuration(Constants.equalitySign dateSign, LocalDateTime startDateTime, Constants.equalitySign durationSign, int duration, int page, int size) {
        Specification<Cruise> specification = (root, query, criteriaBuilder) -> {
            Expression<Integer> timeDiff = criteriaBuilder.function("tsrange_subdiff", Integer.class, root.<Timestamp>get("endDateTime"), root.<Timestamp>get("startDateTime"));
            List<Predicate> predicateList = new ArrayList<>();
            if (startDateTime != null) {
                predicateList.add(getDateTimeWithSign(dateSign, startDateTime, root, criteriaBuilder));
            }
            if (duration > 0) {
                predicateList.add(getDurationWithSign(durationSign, duration, criteriaBuilder, timeDiff));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(page, size);
        return cruiseRepository.findAll(specification, pageable);
    }

    public Cruise findByName(String name) {
        return cruiseRepository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find cruise by name: not exists"));
    }

    public boolean existsByName(String name) {
        return cruiseRepository.existsByName(name);
    }

    private Predicate getDurationWithSign(Constants.equalitySign durationSign, int duration, CriteriaBuilder criteriaBuilder, Expression<Integer> timeDiff) {
        duration = duration * 86400;
        if (durationSign != null && durationSign.equals(Constants.equalitySign.GT))
            return criteriaBuilder.greaterThan(timeDiff, duration);
        else if (durationSign != null && durationSign.equals(Constants.equalitySign.LT))
            return criteriaBuilder.lessThan(timeDiff, duration);
        else
            return criteriaBuilder.equal(timeDiff, duration);
    }

    private Predicate getDateTimeWithSign(Constants.equalitySign dateSign, LocalDateTime startDateTime, Root<Cruise> root, CriteriaBuilder criteriaBuilder) {
        if (dateSign != null && dateSign.equals(Constants.equalitySign.GT))
            return criteriaBuilder.greaterThan(root.get("startDateTime"), startDateTime);
        else if (dateSign != null && dateSign.equals(Constants.equalitySign.LT))
            return criteriaBuilder.lessThan(root.get("startDateTime"), startDateTime);
        else
            return criteriaBuilder.equal(root.get("startDateTime"), startDateTime);
    }

    public Cruise findById(long id) {
        return cruiseRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find cruise by id: not exists [id=" + id + "]"));
    }

    public void update(Cruise cruise) {
        if (!cruiseRepository.existsById(cruise.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update cruise: not exists [id=" + cruise.getId() + "]");
        cruiseRepository.save(cruise);
    }

    public void create(Cruise cruise) {
        if (cruise.getId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create cruise: already exists [id=" + cruise.getId() + "]");
        cruiseRepository.save(cruise);
    }

    public List<CruiseStatus> findAllStatuses() {
        return statusDictRepository.findAll();
    }

    public CruiseStatus findStatusById(long id) {
        return statusDictRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find cruise status by id " + id));
    }

    public void delete(long id) {
        if (!cruiseRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to find cruise by id " + id + " for delete");
        cruiseRepository.deleteById(id);
    }
}
