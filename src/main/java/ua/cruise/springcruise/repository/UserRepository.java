package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByOrderByIdAsc();

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String name);
}