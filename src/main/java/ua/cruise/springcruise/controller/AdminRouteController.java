package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.service.RouteService;

import java.util.List;

@Controller
@RequestMapping("/admin-route")
public class AdminRouteController {
    private final RouteService routeService;

    private static final String REDIRECT_URL = "redirect:/admin-route";

    @Autowired
    public AdminRouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Route> routeList = routeService.findAll();
        model.addAttribute("routeList", routeList);
        return "admin/route/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Route route = routeService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route not exists"));
        model.addAttribute("route", route);
        return "admin/route/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("route") Route route, @PathVariable("id") Long id) {
        routeService.update(route);
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(@ModelAttribute("route") Route route) {
        return "admin/route/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("route") Route route) {
        try {
            routeService.create(route);
        } catch (ResponseStatusException ex){
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        routeService.delete(id);
        return REDIRECT_URL;
    }
}
