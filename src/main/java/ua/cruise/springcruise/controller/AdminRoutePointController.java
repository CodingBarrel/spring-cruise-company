package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.service.RoutePointService;
import ua.cruise.springcruise.service.RouteService;

import java.util.List;

@Controller
@RequestMapping("/admin-routepoint")
public class AdminRoutePointController {
    private final RoutePointService routePointService;
    private final RouteService routeService;

    private static final String REDIRECT_URL = "redirect:/admin-route";

    @Autowired
    public AdminRoutePointController(RoutePointService routePointService, RouteService routeService) {
        this.routePointService = routePointService;
        this.routeService = routeService;
    }

    @GetMapping("/{id}")
    public String readByRouteId(@PathVariable Long id, Model model) {
        List<RoutePoint> routePointList = routePointService.findByRouteId(id);
        model.addAttribute("routeId", id);
        model.addAttribute("routePointList", routePointList);
        return "admin/routePoint/readById";
    }

    @GetMapping("/{routeId}/edit/{pointId}")
    public String updateForm(@ModelAttribute("routePoint") RoutePoint routePoint, @PathVariable("routeId") Long routeId, @PathVariable("pointId") Long pointId, Model model) {
        List<RoutePoint> routePointList = routePointService.findByRouteId(routeId);
        model.addAttribute("pointId", pointId);
        model.addAttribute("routePointList", routePointList);
        return "admin/routePoint/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("routePoint") RoutePoint routePoint, @PathVariable("id") Long id) {
        try {
            routePoint.setId(id);
            routePointService.update(routePoint);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @PostMapping("/{id}")
    public String create(@PathVariable Long id) {
        try {
            RoutePoint routePoint = new RoutePoint();
            Route route = routeService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            routePoint.setRoute(route);
            routePointService.create(routePoint);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        routePointService.delete(id);
        return REDIRECT_URL;
    }
}
