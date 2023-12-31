package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.cruise.springcruise.repository.UserRepository;

/**
 * A service class that connects controller and model layers, thereby isolating business-logic.Controls requests
 * user details for security.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Service
 * @see UserDetailsService
 */

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).orElseThrow( () ->
                new UsernameNotFoundException("Failed to find user by login: not exists [login=" + username + "]"));
    }
}
