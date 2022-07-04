package ua.cruise.springcruise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.TicketService;

import java.util.List;

@Controller
@RequestMapping("/admin-ticket")
public class AdminTicketController {
    private final TicketService ticketService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:admin-route/";

    @Autowired
    public AdminTicketController(TicketService ticketService, EntityMapper mapper) {
        this.ticketService = ticketService;
        this.mapper = mapper;
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
}
