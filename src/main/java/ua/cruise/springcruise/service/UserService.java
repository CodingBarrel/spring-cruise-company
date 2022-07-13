package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.repository.UserRepository;
import ua.cruise.springcruise.repository.dict.UserRoleRepository;

import java.util.List;

/**
 * A service class that connects controller and model layers, thereby isolating business-logic. Controls requests
 * related to user and it's status.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Service
 * @see User
 */

@Log4j2
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository roleDictRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository roleDictRepository) {
        this.userRepository = userRepository;
        this.roleDictRepository = roleDictRepository;
    }

    public List<User> findAll() {
        return userRepository.findByOrderByIdAsc();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find user by id: not exists [id=" + id + "]"));
    }

    public boolean existsByLogin(String login){
        return userRepository.existsByLogin(login);
    }

    public void create(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create user: already exists [id=" + user.getId() + "]");
        userRepository.save(user);
    }

    public void update(User user) {
        if (!userRepository.existsById(user.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user: not exists [id=" + user.getId() + "]");
        userRepository.save(user);
    }

    public List<UserRole> findRoleDict() {
        return roleDictRepository.findByOrderByIdAsc();
    }

    public UserRole findRoleById(long id) {
        return roleDictRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find role: not exists [id=" + id + "]"));
    }
}
