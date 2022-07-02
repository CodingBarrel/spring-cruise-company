package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final CruiseService cruiseService;

    private static final String REDIRECT_URL = "redirect:admin-route/";

    @Autowired
    public TicketController(TicketService ticketService, CruiseService cruiseService) {
        this.ticketService = ticketService;
        this.cruiseService = cruiseService;
    }

    @GetMapping("")
    public String readByUserId(Model model, HttpSession session) {
        List<Ticket> ticketList = ticketService.findByUserId((Long) session.getAttribute("loggedUser.id"));
        model.addAttribute("ticketList", ticketList);
        return "admin/ticket/readAll";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found"));
        model.addAttribute("ticket", ticket);
        return "admin/ticket/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("ticket") Ticket ticket, @PathVariable("id") Long id) {
        try {
            ticketService.update(ticket);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }

    @GetMapping("{id}/new")
    public String createForm(@ModelAttribute("ticket") Ticket ticket, @PathVariable("id") Long id, Model model) {
        Cruise cruise = cruiseService.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<Ticket> ticketList = ticketService.findByCruiseActual(cruise);
        model.addAttribute("ticketList", ticketList);
        return "ticket/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("ticket") Ticket ticket) {
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
