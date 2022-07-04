package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.TicketService;

import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final CruiseService cruiseService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:admin-route/";

    @Autowired
    public TicketController(TicketService ticketService, CruiseService cruiseService, EntityMapper mapper) {
        this.ticketService = ticketService;
        this.cruiseService = cruiseService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String readByUserId(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<Ticket> ticketList = ticketService.findByUserId(user.getId());
        model.addAttribute("ticketList", ticketList);
        return "admin/ticket/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found"));
        TicketDTO ticketDTO = mapper.ticketToDTO(ticket);
        model.addAttribute("ticketDTO", ticketDTO);
        return "admin/ticket/update";
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
        List<Ticket> ticketList = ticketService.findByCruiseActual(cruise);
        model.addAttribute("ticketDTO", ticketDTO);
        model.addAttribute("ticketList", ticketList);
        return "ticket/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("ticketDTO") TicketDTO ticketDTO) {
        Ticket ticket = mapper.dtoToTicket(ticketDTO);
        ticket.setStatus(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID));
        try {
            ticketService.create(ticket);
        } catch (ResponseStatusException ex) {
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
