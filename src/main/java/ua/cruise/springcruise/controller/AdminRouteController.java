package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.RouteDTO;
import ua.cruise.springcruise.dto.RoutePointDTO;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.service.RoutePointService;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.RouteService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Log4j2
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
    public String update(@PathVariable("id") Long id, @ModelAttribute("routeDTO") @Valid RouteDTO routeDTO, BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RouteDTO is not valid");
        Route route = mapper.dtoToRoute(routeDTO);
        route.setId(id);
        if (routeService.existsByName(route.getName()) &&
                !Objects.equals(routeService.findByName(route.getName()).getId(), route.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Failed to update route [id=" + id + "]");
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
    public String create(@ModelAttribute("routeDTO") @Valid RouteDTO routeDTO, BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RouteDTO is not valid");
        Route route = mapper.dtoToRoute(routeDTO);
        if (routeService.existsByName(route.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create route: name already taken [id=" + route.getId() + "]");
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
                              @ModelAttribute("routePointDTO") @Valid RoutePointDTO routePointDTO, BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RoutepointDTO is not valid");
        RoutePoint routePoint = mapper.dtoToRoutePoint(routePointDTO);
        routePoint.setId(pointId);
        routePoint.setRoute(routeService.findById(routeId));
        if (routePointService.existsByName(routePoint.getName()) &&
                !Objects.equals(routePointService.findByName(routePoint.getName()).getId(), routePoint.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update routepoint: name already taken[name=" + routePoint.getName() + "]");
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
