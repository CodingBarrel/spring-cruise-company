package ua.cruise.springcruise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.LinerDTO;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.LinerService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An controller class that controls authorized as administrators users' requests related to liner administration
 * (such as CRUD operations) and redirects them to requested services. Controls view layer.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Controller
 */

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin-liner")
public class AdminLinerController implements BaseController {
    private final LinerService linerService;
    private final EntityMapper mapper;

    protected static final Map<Long, String> currentlyModifiedLiners = new ConcurrentReferenceHashMap<>();

    private static final String REDIRECT_URL = "redirect:/admin-liner";

    @GetMapping("")
    public String readAll(Model model) {
        List<Liner> linerList = linerService.findAll();
        model.addAttribute("linerList", linerList);
        return "admin/liner/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        checkModifiedObjectsConflict(currentlyModifiedLiners, id);
        Liner liner = linerService.findById(id);
        LinerDTO linerDTO = mapper.linerToDTO(liner);
        model.addAttribute("linerDTO", linerDTO);
        return "admin/liner/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("linerDTO") @Valid LinerDTO linerDTO,
                         BindingResult result) {
        checkModifiedObjectsConflict(currentlyModifiedLiners, id);
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LinerDTO is not valid");
        Liner liner = mapper.dtoToLiner(linerDTO);
        liner.setId(id);
        if (linerService.existsByName(liner.getName()) &&
                !Objects.equals(linerService.findByName(liner.getName()).getId(), liner.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Failed to update liner: name already taken [name= " + liner.getName() + "]");
        try {
            linerService.update(liner);
        } catch (ResponseStatusException ex) {
            return "/error/501";
        }
        currentlyModifiedLiners.remove(id);
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        LinerDTO linerDTO = new LinerDTO();
        model.addAttribute("linerDTO", linerDTO);
        return "admin/liner/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("linerDTO") @Valid LinerDTO linerDTO, BindingResult result) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LinerDTO is not valid");
        Liner liner = mapper.dtoToLiner(linerDTO);
        if (linerService.existsByName(liner.getName()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Failed to create liner: name already taken [name=" + liner.getName() + "]");
        try {
            linerService.create(liner);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        checkModifiedObjectsConflict(currentlyModifiedLiners, id);
        linerService.delete(id);
        currentlyModifiedLiners.remove(id);
        return REDIRECT_URL;
    }
}
