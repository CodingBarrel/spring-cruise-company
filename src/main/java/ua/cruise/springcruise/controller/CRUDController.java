package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.repository.RouteRepository;

import java.util.List;

@Controller
@RequestMapping("/route")
public class CRUDController {
    private final RouteRepository routeRepository;

    @Autowired
    public CRUDController(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Route> routeList = routeRepository.findAll();
        model.addAttribute("routeList", routeList);
        return "route/read";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable Long id, Model model) {
        model.addAttribute("route", routeRepository.findById(id));
        return "route/readById";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("route", routeRepository.findById(id));
        return "route/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("route") Route route, @PathVariable("id") Long id) {
        if (routeRepository.existsById(id)) {
            route.setId(id);
            routeRepository.save(route);
        }
        return "redirect:/route";
    }

    @GetMapping("/new")
    public String insertForm(@ModelAttribute("route") Route route) {
        return "route/new";
    }

    @PutMapping()
    public String insert(@ModelAttribute("route") Route route) {
        routeRepository.save(route);
        return "redirect:/route";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        routeRepository.deleteById(id);
        return "redirect:/route";
    }
}
