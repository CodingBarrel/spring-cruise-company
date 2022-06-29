package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRoleDict;
import ua.cruise.springcruise.repository.UserRepository;
import ua.cruise.springcruise.repository.dict.UserRoleDictRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleDictRepository roleDictRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleDictRepository roleDictRepository) {
        this.userRepository = userRepository;
        this.roleDictRepository = roleDictRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public void create(User user) {
        userRepository.findByLogin(user.getLogin()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        });
        user.setRole(roleDictRepository.findById(1L).orElseThrow( () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        userRepository.save(user);
    }

    @Transactional
    public void update(User user){
        if (userRepository.existsById(user.getId()))
            userRepository.save(user);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not existst");
    }

    public List<UserRoleDict> findRoleDict(){
        return roleDictRepository.findAll();
    }
}
