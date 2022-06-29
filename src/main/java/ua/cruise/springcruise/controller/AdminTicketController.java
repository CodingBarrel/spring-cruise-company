package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.service.TicketService;

import java.util.List;

@Controller
@RequestMapping("/admin-ticket")
public class AdminTicketController {
    private final TicketService ticketService;

    private static final String REDIRECT_URL = "redirect:admin-route/";

    @Autowired
    public AdminTicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public String readAll(Model model) {
        List<Ticket> ticketList = ticketService.findAll();
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
        }catch (ResponseStatusException ex){
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }
}
