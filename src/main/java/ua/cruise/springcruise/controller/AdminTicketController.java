package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.TicketService;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/admin-ticket")
public class AdminTicketController {
    private final TicketService ticketService;
    private final EntityMapper mapper;

    private static final String REDIRECT_URL = "redirect:/admin-ticket";

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
        Ticket ticket = ticketService.findById(id);
        TicketDTO ticketDTO = mapper.ticketToDTO(ticket);
        List<TicketStatus> statusList = ticketService.findStatusDict();
        model.addAttribute("ticketDTO", ticketDTO);
        model.addAttribute("statusList", statusList);
        return "admin/ticket/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("ticketDTO") TicketDTO ticketDTO, BindingResult result) {
        Ticket ticket = ticketService.findById(id);
        TicketStatus status = ticketService.findStatusById(ticketDTO.getStatus().getId());
        ticket.setStatus(status);
        try {
            ticketService.update(ticket);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        return REDIRECT_URL;
    }
}
