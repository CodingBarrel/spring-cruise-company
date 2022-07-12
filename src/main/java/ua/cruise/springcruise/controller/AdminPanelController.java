package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Log4j2
@Controller
@RequestMapping("/admin-panel")
public class AdminPanelController {

    @GetMapping("")
    public String getAdminPanel(Model model){
        model.addAttribute("currentlyModifiedCruises", AdminCruiseController.currentlyModifiedCruises);
        model.addAttribute("currentlyModifiedLiners", AdminLinerController.currentlyModifiedLiners);
        model.addAttribute("currentlyModifiedRoutes", AdminRouteController.currentlyModifiedRoutes);
        model.addAttribute("currentlyModifiedTickets", AdminTicketController.currentlyModifiedTickets);
        model.addAttribute("currentlyModifiedUsers", AdminUserController.currentlyModifiedUsers);
        return "admin/panel";
    }

}
