package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.Liner;

public interface LinerRepository extends JpaRepository<Liner, Long> {

}