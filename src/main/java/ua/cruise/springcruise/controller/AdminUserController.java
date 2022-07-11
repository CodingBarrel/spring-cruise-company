package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.UserDTO;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin-user")
public class AdminUserController {
    private final UserService userService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:admin-route/";

    @Autowired
    public AdminUserController(UserService userService, EntityMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        return "admin/user/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        UserDTO userDTO = mapper.userToDTO(user);
        List<UserRole> roleDict = userService.findRoleDict();
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("roleDict", roleDict);
        return "admin/user/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("userDTO") UserDTO userDTO) {
        User user = userService.findById(id);
        UserRole role = userService.findRoleById(userDTO.getRole().getId());
        user.setRole(role);
        try {
            userService.update(user);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

}
