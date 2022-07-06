package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.LinerDTO;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.LinerService;

import java.util.List;

@Controller
@RequestMapping("/admin-liner")
public class AdminLinerController {
    private final LinerService linerService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/admin-liner";

    @Autowired
    public AdminLinerController(LinerService linerService, EntityMapper mapper) {
        this.linerService = linerService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Liner> linerList = linerService.findAll();
        model.addAttribute("linerList", linerList);
        return "admin/liner/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Liner liner = linerService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Liner not found"));
        LinerDTO linerDTO = mapper.linerToDTO(liner);
        model.addAttribute("linerDTO", linerDTO);
        return "admin/liner/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("linerDTO") LinerDTO linerDTO) {
        Liner liner = mapper.dtoToLiner(linerDTO);
        liner.setId(id);
        try {
            linerService.update(liner);
        } catch (ResponseStatusException ex) {
            return "/error/501";
        }
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        LinerDTO linerDTO = new LinerDTO();
        model.addAttribute("linerDTO", linerDTO);
        return "admin/liner/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("linerDTO") LinerDTO linerDTO) {
        Liner liner = mapper.dtoToLiner(linerDTO);
        try {
            linerService.create(liner);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        linerService.delete(id);
        return REDIRECT_URL;
    }
}
