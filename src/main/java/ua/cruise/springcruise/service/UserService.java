package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.repository.UserRepository;
import ua.cruise.springcruise.repository.dict.UserRoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepository userRepository;
    private final UserRoleRepository roleDictRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository roleDictRepository) {
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
        userRepository.save(user);
    }

    @Transactional
    public void update(User user){
            userRepository.save(user);
    }

    public List<UserRole> findRoleDict(){
        return roleDictRepository.findAll();
    }

    public UserRole findRoleById(long id){
        return roleDictRepository.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
