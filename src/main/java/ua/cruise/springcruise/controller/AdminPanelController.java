package ua.cruise.springcruise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController {

    @GetMapping("")
    public String getAdminPanel(){
        return "admin/panel";
    }

}
