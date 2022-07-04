package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}