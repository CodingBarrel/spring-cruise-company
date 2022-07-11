package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Log4j2
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
        Cruise cruise = cruiseService.findById(id);
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
    public String update(@PathVariable("id") long id, @ModelAttribute("cruiseDTO") @Valid CruiseDTO cruiseDTO,
                         BindingResult result, @RequestParam(value = "image", required = false) MultipartFile file) {
        if (result.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CruiseDTO is not valid");
        Cruise cruise = entityMapper.dtoToCruise(cruiseDTO);
        cruise.setId(id);
        if (cruiseService.existsByName(cruise.getName()) &&
                !Objects.equals(cruiseService.findByName(cruise.getName()).getId(), cruise.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Failed to update cruise: name is already taken");
        Path fileDirectory = null;
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
            fileDirectory = Path.of(Constants.DATA_PATH + "/cruise/");
            try {
                fileName = storageService.save(fileDirectory, fileExt, file);
                String oldFileName = cruise.getImageName();
                cruise.setImageName(fileName);
                cruiseService.update(cruise);
                storageService.delete(Path.of(fileDirectory + oldFileName));
            } catch (IOException ex) {
                log.info("Failed to save image for cruise. Trying to delete", ex);
                try {
                    storageService.delete(Path.of(fileDirectory + fileName));
                } catch (IOException internalEx) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete file [dir=" + fileDirectory + "], file=[" + fileName + "]", internalEx);
                }
            }
        } else {
            cruise.setImageName(cruiseService.findById(cruise.getId()).getImageName());
            cruiseService.update(cruise);
        }
        return REDIRECT_URL;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        CruiseDTO cruiseDTO = new CruiseDTO();
        List<Liner> linerList = linerService.findAll();
        List<Route> routeList = routeService.findAll();
        model.addAttribute("cruiseDTO", cruiseDTO);
        model.addAttribute("linerList", linerList);
        model.addAttribute("routeList", routeList);
        return "admin/cruise/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("cruiseDTO") @Valid CruiseDTO cruiseDTO, BindingResult result,
                         @RequestParam("image") MultipartFile file, Model model) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CruiseDTO is not valid");
        }
        Cruise cruise = entityMapper.dtoToCruise(cruiseDTO);
        cruise.setStatus(cruiseService.findStatusById(Constants.CRUISE_DEFAULT_STATUS_ID));
        if (cruiseService.existsByName(cruise.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create cruise: name is already taken");
        String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path fileDirectory = null;
        String fileName = null;
        try {
            fileDirectory = Path.of(Constants.DATA_PATH + "/cruise/");
            fileName = storageService.save(fileDirectory, fileExt, file);
            cruise.setImageName(fileName);
        } catch (IOException ex) {
            log.info("Failed to save image for cruise. Trying to delete", ex);
            try {
                storageService.delete(Path.of(fileDirectory + fileName));
            } catch (IOException internalEx) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete file [dir=" + fileDirectory + "], file=[" + fileName + "]", internalEx);
            }
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
