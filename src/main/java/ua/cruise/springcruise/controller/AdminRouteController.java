package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.RouteDTO;
import ua.cruise.springcruise.dto.RoutePointDTO;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.service.RoutePointService;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.RouteService;

import java.util.List;

@Controller
@RequestMapping("/admin-route")
public class AdminRouteController {
    private final RouteService routeService;
    private final RoutePointService routePointService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/admin-route";

    @Autowired
    public AdminRouteController(RouteService routeService, RoutePointService routePointService,
                                EntityMapper mapper) {
        this.routeService = routeService;
        this.routePointService = routePointService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Route> routeList = routeService.findAll();
        model.addAttribute("routeList", routeList);
        return "admin/route/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Route route = routeService.findById(id);
        RouteDTO routeDTO = mapper.routeToDTO(route);
        model.addAttribute("routeDTO", routeDTO);
        return "admin/route/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("routeDTO") RouteDTO routeDTO) {
        Route route = mapper.dtoToRoute(routeDTO);
        routeService.update(route);
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        RouteDTO routeDTO = new RouteDTO();
        model.addAttribute("routeDTO", routeDTO);
        return "admin/route/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("routeDTO") RouteDTO routeDTO) {
        Route route = mapper.dtoToRoute(routeDTO);
        try {
            routeService.create(route);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        routeService.delete(id);
        return REDIRECT_URL;
    }

    @GetMapping("/{routeId}/point")
    public String readPointByRouteId(@PathVariable Long routeId, Model model) {
        List<RoutePoint> routePointList = routePointService.findByRouteId(routeId);
        model.addAttribute("routeId", routeId);
        model.addAttribute("routePointList", routePointList);
        return "admin/routePoint/readById";
    }

    @GetMapping("/{routeId}/point/{pointId}/edit")
    public String pointUpdateForm(@PathVariable("routeId") Long routeId,
                                  @PathVariable("pointId") Long pointId, Model model) {
        RoutePoint routePoint = routePointService.findById(pointId);
        RoutePointDTO routePointDTO = mapper.routePointToDTO(routePoint);
        List<RoutePoint> routePointList = routePointService.findByRouteId(routeId);
        model.addAttribute("routePointList", routePointList);
        model.addAttribute("routePointDTO", routePointDTO);
        return "admin/routePoint/update";
    }

    @PatchMapping("/{routeId}/point/{pointId}")
    public String pointUpdate(@PathVariable("routeId") Long routeId,
                              @PathVariable("pointId") Long pointId,
                              @ModelAttribute("routePointDTO") RoutePointDTO routePointDTO) {
        RoutePoint routePoint = mapper.dtoToRoutePoint(routePointDTO);
        routePoint.setId(pointId);
        routePoint.setRoute(routeService.findById(routeId));
        try {
            routePointService.update(routePoint);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL + "/" + routeId + "/point";
    }

    @PostMapping("/{routeId}/point")
    public String pointCreate(@PathVariable Long routeId) {
        try {
            RoutePoint routePoint = new RoutePoint();
            Route route = routeService.findById(routeId);
            routePoint.setRoute(route);
            routePointService.create(routePoint);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL + "/" + routeId + "/point";
    }

    @DeleteMapping("/{routeId}/point/{pointId}")
    public String pointDelete(@PathVariable("routeId") Long routeId,
                              @PathVariable("pointId") Long pointId) {
        routePointService.delete(pointId);
        return REDIRECT_URL + "/" + routeId + "/point";
    }
}
