package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private static final String REDIRECT_URL = "redirect:/";

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String signUpForm(@ModelAttribute("user") User user) {
        return "user/create";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("userDTO") UserDTO userDTO) {
        User user = mapper.dtoToUser(userDTO);
        user.setRole(userService.findRoleById(Constants.USER_DEFAULT_ROLE_ID));
        try {
            userService.create(user);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

}
