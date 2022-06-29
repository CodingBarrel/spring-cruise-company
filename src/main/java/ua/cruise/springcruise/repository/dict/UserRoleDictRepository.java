package ua.cruise.springcruise.repository.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cruise.springcruise.entity.dictionary.UserRoleDict;

public interface UserRoleDictRepository extends JpaRepository<UserRoleDict, Long> {
}