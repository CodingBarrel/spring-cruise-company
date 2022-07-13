package ua.cruise.springcruise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.repository.UserRepository;
import ua.cruise.springcruise.repository.dict.UserRoleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    User testUser1;
    User testUser2;
    User testUser3;

    UserRole testUserRole1;
    UserRole testUserRole2;

    @BeforeEach
    void setUp() {
        testUserRole1 = new UserRole(1L, "Test user role 1");
        testUserRole2 = new UserRole(2L, "Test user role 2");

        testUser1 = new User(1L, "Log1", "Pass1", testUserRole1, LocalDateTime.now());
        testUser2 = new User(2L, "Log2", "Pass2", testUserRole2, LocalDateTime.now());
        testUser3 = new User(3L, "Log3", "Pass3", testUserRole1, LocalDateTime.now());
    }

    @Test
    void findAllSuccessCase() {
        List<User> testUserList = List.of(testUser1, testUser2, testUser3);
        when(userRepository.findByOrderByIdAsc()).thenReturn(testUserList);
        assertEquals(testUserList, userService.findAll());
        verify(userRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllEmptyCase() {
        List<User> testUserList = List.of();
        when(userRepository.findByOrderByIdAsc()).thenReturn(testUserList);
        assertEquals(0, userService.findAll().size());
        verify(userRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findByIdSuccessCase() {
        when(userRepository.findById(testUser1.getId())).thenReturn(Optional.ofNullable(testUser1));
        assertEquals(testUser1, userService.findById(testUser1.getId()));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFoundCase() {
        when(userRepository.findById(testUser1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.findById(testUser1.getId()));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void existsByLoginSuccessCase() {
        when(userRepository.existsByLogin(testUser1.getLogin())).thenReturn(true);
        assertTrue(userService.existsByLogin(testUser1.getLogin()));
        verify(userRepository, times(1)).existsByLogin(anyString());
    }

    @Test
    void existsByLoginNotExistsCase() {
        when(userRepository.existsByLogin(testUser1.getLogin())).thenReturn(false);
        assertFalse(userService.existsByLogin(testUser1.getLogin()));
        verify(userRepository, times(1)).existsByLogin(anyString());
    }

    @Test
    void createSuccessCase() {
        testUser1.setId(null);
        userService.create(testUser1);
        verify(userRepository, times(0)).existsById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createAlreadyExistsCase() {
        when(userRepository.existsById(testUser1.getId())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> userService.create(testUser1));
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateSuccessCase() {
        when(userRepository.existsById(testUser1.getId())).thenReturn(true);
        userService.update(testUser1);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateNotExistsCase() {
        when(userRepository.existsById(testUser1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> userService.update(testUser1));
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void findRoleDictSuccessCase() {
        List<UserRole> testUserRoleList = List.of(testUserRole1, testUserRole2);
        when(userRoleRepository.findByOrderByIdAsc()).thenReturn(testUserRoleList);
        assertEquals(testUserRoleList, userService.findRoleDict());
        verify(userRoleRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findRoleDictEmptyCase() {
        when(userRoleRepository.findByOrderByIdAsc()).thenReturn(List.of());
        assertEquals(0, userService.findRoleDict().size());
        verify(userRoleRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findRoleByIdSuccessCase() {
        when(userRoleRepository.findById((long) testUserRole1.getId())).thenReturn(Optional.ofNullable(testUserRole1));
        assertEquals(testUserRole1, userService.findRoleById(testUserRole1.getId()));
        verify(userRoleRepository, times(1)).findById(anyLong());
    }

    @Test
    void findRoleByIdNotFoundCase() {
        when(userRoleRepository.findById(testUserRole1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.findRoleById(testUserRole1.getId()));
        verify(userRoleRepository, times(1)).findById(anyLong());
    }
}