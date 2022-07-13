package ua.cruise.springcruise.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.UserDTO;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.service.UserService;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapperImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    UserService userService;

    @Mock
    EntityMapperImpl mapper;

    @Mock
    Model model;

    @Mock
    BindingResult result;

    UserRole testUserRole;

    UserDTO testUserDTO;


    @BeforeEach
    void setUp() {
        testUserRole = new UserRole(1L, "Test user role 1");
        testUserDTO = new UserDTO(191L, "log", "pass", testUserRole, LocalDateTime.now());
    }

    @Test
    void signUpFormSuccessCase() {
        assertEquals("user/create", authController.signUpForm(model));
        verify(model, times(1)).addAttribute(eq("userDTO"), any(UserDTO.class));
    }

    @Test
    void signUpSuccessCase() {
        when(result.hasErrors()).thenReturn(false);
        when(mapper.dtoToUser(any(UserDTO.class))).thenCallRealMethod();
        when(userService.findRoleById(Constants.USER_DEFAULT_ROLE_ID)).thenReturn(testUserRole);
        when(userService.existsByLogin(testUserDTO.getLogin())).thenReturn(false);
        authController.signUp(testUserDTO, result);
        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void signUpInvalidDTOCase() {
        when(result.hasErrors()).thenReturn(true);
        assertEquals("user/create", authController.signUp(testUserDTO, result));
        verify(userService, times(0)).create(any(User.class));
    }

    @Test
    void signUpAlreadyExistsCase() {
        when(result.hasErrors()).thenReturn(false);
        when(mapper.dtoToUser(any(UserDTO.class))).thenCallRealMethod();
        when(userService.findRoleById(Constants.USER_DEFAULT_ROLE_ID)).thenReturn(testUserRole);
        when(userService.existsByLogin(testUserDTO.getLogin())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> authController.signUp(testUserDTO, result));
        verify(userService, times(0)).create(any(User.class));
    }
}