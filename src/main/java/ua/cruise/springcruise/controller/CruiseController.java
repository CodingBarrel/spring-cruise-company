package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.service.CruiseService;

import java.util.List;

@Controller
@RequestMapping("/cruise")
public class CruiseController {
    private final CruiseService cruiseService;

    private static final String REDIRECT_URL = "redirect:/cruise";

    @Autowired
    public CruiseController(CruiseService cruiseService) {
        this.cruiseService = cruiseService;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Cruise> cruiseList = cruiseService.findAll();
        model.addAttribute("cruiseList", cruiseList);
        return "cruise/readAll";
    }

    @GetMapping("/{id}")
    public String readById(@PathVariable Long id, Model model) {
        Cruise foundCruise = cruiseService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cruise not found"));
        model.addAttribute("cruise", foundCruise);
        return "cruise/readById";
    }
}
