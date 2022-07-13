package ua.cruise.springcruise.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

/**
 * An controller class that controls authorized users' requests related to ticket (such as create ticket) and redirects
 * them to requested services. Controls view layer.
 *
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Controller
 */

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final CruiseService cruiseService;
    private final StorageService storageService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/ticket";

    @GetMapping("")
    public String readByUserId(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<Ticket> ticketList = ticketService.findByUserId(user.getId());
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("ticketList", ticketList);
        return "ticket/readMy";
    }

    @GetMapping("{id}/new")
    public String createForm(@PathVariable("id") Long id, Model model) {
        TicketDTO ticketDTO = new TicketDTO();
        Cruise cruise = cruiseService.findById(id);
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
        Cruise cruise = cruiseService.findById(id);
        ticket.setCruise(cruise);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        ticket.setUser(user);
        ticket.setPrice(cruise.getPrice());
        ticket.setStatus(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID));
        if (ticketService.exists(cruise, ticket.getPosition()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create ticket: position already taken [pos=" + ticket.getPosition() + "]");
        Path fileDirectory;
        String fileName;
        try {
            fileDirectory = Path.of(Constants.DATA_PATH + "/docs/");
            fileName = storageService.save(fileDirectory, fileExt, file);
            ticket.setImageName(fileName);
            ticketService.create(ticket);
            cruiseService.updateCruiseStatusDueToCapacity(cruise);
        } catch (ResponseStatusException | IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save image for ticket", ex);
        }
        return REDIRECT_URL;
    }

    @PatchMapping("{id}")
    public String changeStatus(@PathVariable Long id, @RequestParam("status") long statusId) {
        if (statusId == Constants.TICKET_PAYED_STATUS)
            ticketService.pay(id);
        else if (statusId == Constants.TICKET_CANCELED_STATUS)
            ticketService.cancel(id);
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update ticket status: wrong id [id=" + id + "]");
        cruiseService.updateCruiseStatusDueToCapacity(ticketService.findById(id).getCruise());
        return REDIRECT_URL;
    }
}
