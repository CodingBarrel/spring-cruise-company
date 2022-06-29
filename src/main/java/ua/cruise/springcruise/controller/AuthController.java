package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private static final String REDIRECT_URL = "redirect:auth/";

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String signUpForm(@ModelAttribute("user") User user) {
        return "admin/user/create";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("user") User user) {
        try {
            userService.create(user);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("/signIn")
    public String signInForm(@ModelAttribute("user") User user) {
        return "admin/user/create";
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute("user") User user) {
        User foundUser;
        try {
            foundUser = userService.findByLogin(user.getLogin()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
            return "/";
        }
        if (user.getPassword().equals(foundUser.getPassword()))
            System.out.println(user.getLogin() + "AUTHORIZED");
        return REDIRECT_URL;
    }

}
