package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.CruiseDTO;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.service.StorageService;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.LinerService;
import ua.cruise.springcruise.service.RouteService;
import ua.cruise.springcruise.util.Constants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/admin-cruise")
public class AdminCruiseController {
    private final CruiseService cruiseService;
    private final LinerService linerService;
    private final RouteService routeService;
    private final StorageService storageService;
    private final EntityMapper entityMapper;


    private static final String REDIRECT_URL = "redirect:/admin-cruise";

    @Autowired
    public AdminCruiseController(CruiseService cruiseService, LinerService linerService, RouteService routeService, StorageService storageService, EntityMapper entityMapper) {
        this.cruiseService = cruiseService;
        this.linerService = linerService;
        this.routeService = routeService;
        this.storageService = storageService;
        this.entityMapper = entityMapper;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Cruise> cruiseList = cruiseService.findAll();
        model.addAttribute("cruiseList", cruiseList);
        return "admin/cruise/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Cruise cruise = cruiseService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cruise not found"));
        CruiseDTO cruiseDTO = entityMapper.cruiseToDTO(cruise);
        List<Liner> linerList = linerService.findAll();
        List<Route> routeList = routeService.findAll();
        List<CruiseStatus> statusList = cruiseService.findAllStatuses();
        model.addAttribute("cruiseDTO", cruiseDTO);
        model.addAttribute("linerList", linerList);
        model.addAttribute("routeList", routeList);
        model.addAttribute("statusList", statusList);

        return "admin/cruise/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") long id, @ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO,
                         @RequestParam(value = "image", required = false) MultipartFile file) {
        Cruise cruise = entityMapper.dtoToCruise(cruiseDTO);
        cruise.setId(id);
        Path fileDirectory = null;
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
            fileDirectory = Path.of(Constants.DATA_PATH + "/cruise/");
            try {
                fileName = storageService.save(fileDirectory, fileExt, file);
                cruise.setImageName(fileName);
            } catch (IOException ex) {
                storageService.delete(Path.of(fileDirectory + fileName));
            }
        } else
            cruise.setImageName(cruiseService.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)).getImageName());
        cruiseService.update(cruise);
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        CruiseDTO cruiseDTO = new CruiseDTO();
        List<Liner> linerList;
        List<Route> routeList;
        List<CruiseStatus> statusList;
        try {
            linerList = linerService.findAll();
            routeList = routeService.findAll();
            statusList = cruiseService.findAllStatuses();
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
            return "/error";
        }
        model.addAttribute("cruiseDTO", cruiseDTO);
        model.addAttribute("linerList", linerList);
        model.addAttribute("routeList", routeList);
        model.addAttribute("statusList", statusList);
        return "admin/cruise/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("cruiseDTO") CruiseDTO cruiseDTO,
                         @RequestParam("image") MultipartFile file) {
        Cruise cruise = entityMapper.dtoToCruise(cruiseDTO);
        cruise.setStatus(cruiseService.findStatusById(Constants.CRUISE_DEFAULT_STATUS_ID));
        String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path fileDirectory = null;
        String fileName = null;
        try {
            fileDirectory = Path.of(Constants.DATA_PATH + "/cruise/");
            fileName = storageService.save(fileDirectory, fileExt, file);
            cruise.setImageName(fileName);
        } catch (IOException e) {
            storageService.delete(Path.of(fileDirectory + fileName));
            e.printStackTrace();
        }
        cruiseService.create(cruise);
        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cruiseService.delete(id);
        return REDIRECT_URL;
    }
}
