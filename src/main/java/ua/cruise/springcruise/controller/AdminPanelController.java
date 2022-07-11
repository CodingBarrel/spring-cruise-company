package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController {

    @GetMapping("")
    public String getAdminPanel(){
        return "admin/panel";
    }

}
