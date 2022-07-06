package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
Add import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.service.StorageService;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.TicketService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final CruiseService cruiseService;
    private final StorageService storageService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/ticket";

    @Autowired
    public TicketController(TicketService ticketService, CruiseService cruiseService, StorageService storageService, EntityMapper mapper) {
        this.ticketService = ticketService;
        this.cruiseService = cruiseService;
        this.storageService = storageService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String readByUserId(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<Ticket> ticketList = ticketService.findByUserId(user.getId());
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("ticketList", ticketList);
        return "ticket/readMy";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("ticketDTO") TicketDTO ticketDTO) {
        Ticket ticket = mapper.dtoToTicket(ticketDTO);
        ticket.setId(id);
        try {
            ticketService.update(ticket);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("{id}/new")
    public String createForm(@PathVariable("id") Long id, Model model) {
        TicketDTO ticketDTO = new TicketDTO();
        Cruise cruise = cruiseService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ticketDTO.setCruise(cruise);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        ticketDTO.setUser(user);
        List<Ticket> ticketList = ticketService.findByCruiseActual(cruise);
        model.addAttribute("ticketDTO", ticketDTO);
        model.addAttribute("ticketList", ticketList);
        return "ticket/create";
    }

    @PostMapping("/{id}")
    public String create(@PathVariable("id") long id,
                         @ModelAttribute("ticketDTO") TicketDTO ticketDTO,
                         @RequestParam("image") MultipartFile file) {
        String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
        Ticket ticket = mapper.dtoToTicket(ticketDTO);
        Cruise cruise = cruiseService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ticket.setCruise(cruise);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        ticket.setUser(user);
        ticket.setPrice(cruise.getPrice());
        ticket.setStatus(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID));
        Path fileDirectory = null;
        String fileName = null;
        try {
            fileDirectory = Path.of(Constants.DATA_PATH + "/docs/");
            fileName = storageService.save(fileDirectory, fileExt, file);
            ticket.setImageName(fileName);
            ticketService.create(ticket);
        } catch (ResponseStatusException | IOException ex) {
            storageService.delete(Path.of(fileDirectory + fileName));
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("{id}/pay")
    public String pay(@PathVariable Long id) {
        try {
            ticketService.pay(id);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("{id}/cancel")
    public String cancel(@PathVariable Long id) {
        try {
            ticketService.cancel(id);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }
}
