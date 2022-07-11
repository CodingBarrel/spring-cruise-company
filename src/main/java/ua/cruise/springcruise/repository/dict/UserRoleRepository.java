package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByOrderByIdAsc();

    Optional<UserRole> findById(long id);
}