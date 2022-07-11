package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.UserDTO;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/";

    @Autowired
    public AuthController(UserService userService, EntityMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "user/create";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors())
            return "user/create";
        User user = mapper.dtoToUser(userDTO);
        user.setRole(userService.findRoleById(Constants.USER_DEFAULT_ROLE_ID));
        if (userService.existsByLogin(user.getLogin()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create user: login already taken [login=" + user.getLogin() + "]");
        try {
            userService.create(user);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

}
