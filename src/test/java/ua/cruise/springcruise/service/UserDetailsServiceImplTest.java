package ua.cruise.springcruise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    User user1;

    UserRole userRole;

    @BeforeEach
    void setUp() {
        userRole = new UserRole(1L, "Test user role 1");

        user1 = new User(101L, "Userlog1", "Userpass1", userRole, LocalDateTime.now());
    }

    @Test
    void loadUserByUsernameSuccessCase() {
        when(userRepository.findByLogin(user1.getLogin())).thenReturn(Optional.ofNullable(user1));
        userDetailsService.loadUserByUsername(user1.getLogin());
        verify(userRepository, times(1)).findByLogin(anyString());
    }

    @Test
    void loadUserByUsernameNotFoundCase() {
        when(userRepository.findByLogin(user1.getLogin())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(user1.getLogin()));
        verify(userRepository, times(1)).findByLogin(anyString());
    }
}