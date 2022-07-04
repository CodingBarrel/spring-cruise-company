package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.CruiseDTO;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.LinerService;
import ua.cruise.springcruise.service.RouteService;
import ua.cruise.springcruise.util.Constants;

import java.util.List;

@Controller
@RequestMapping("/admin-cruise")
public class AdminCruiseController {
    private final CruiseService cruiseService;
    private final LinerService linerService;
    private final RouteService routeService;


    private static final String REDIRECT_URL = "redirect:/admin-cruise";

    @Autowired
    public AdminCruiseController(CruiseService cruiseService, LinerService linerService, RouteService routeService) {
        this.cruiseService = cruiseService;
        this.linerService = linerService;
        this.routeService = routeService;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Cruise> cruiseList = cruiseService.findAll();
        model.addAttribute("cruiseList", cruiseList);
        return "admin/cruise/readAll";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable Long id, Model model) {
        Cruise cruise = cruiseService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cruise not found"));
        model.addAttribute("cruise", cruise);
        return "admin/cruise/readById";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, @ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO, Model model) {
        Cruise cruise = cruiseService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cruise not found"));
        List<Liner> linerList = linerService.findAll();
        List<Route> routeList = routeService.findAll();
        List<CruiseStatus> statusList = cruiseService.findStatusDict();
        model.addAttribute("cruise", cruise);
        model.addAttribute("linerList", linerList);
        model.addAttribute("routeList", routeList);
        model.addAttribute("statusList", statusList);
        return "admin/cruise/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") long id,@ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO) {
        try {
            cruiseDTO.setId(id);
            cruiseService.update(cruiseDTO);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(@ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO, Model model) {
        List<Liner> linerList;
        List<Route> routeList;
        List<CruiseStatus> statusList;
        try {
            linerList = linerService.findAll();
            routeList = routeService.findAll();
            statusList = cruiseService.findStatusDict();
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
            return "/error";
        }
        model.addAttribute("linerList", linerList);
        model.addAttribute("routeList", routeList);
        model.addAttribute("statusList", statusList);

        return "admin/cruise/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO) {
        Cruise cruise = entityMapper.dtoToCruise(cruiseDTO);
        cruise.setStatus(cruiseService.findStatusById(Constants.CRUISE_DEFAULT_STATUS_ID));
        cruiseService.create(cruise);
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cruiseService.delete(id);
        return REDIRECT_URL;
    }
}
