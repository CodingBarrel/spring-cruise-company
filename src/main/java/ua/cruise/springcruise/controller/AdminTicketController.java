package ua.cruise.springcruise.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.util.EntityMapper;
import ua.cruise.springcruise.service.TicketService;

import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/admin-ticket")
public class AdminTicketController implements BaseController {
    private final TicketService ticketService;
    private final CruiseService cruiseService;
    private final EntityMapper mapper;

    protected static final Map<Long, String> currentlyModifiedTickets = new ConcurrentReferenceHashMap<>();

    private static final String REDIRECT_URL = "redirect:/admin-ticket";

    @Autowired
    public AdminTicketController(TicketService ticketService, CruiseService cruiseService, EntityMapper mapper) {
        this.ticketService = ticketService;
        this.cruiseService = cruiseService;
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
        checkModifiedObjectsConflict(currentlyModifiedTickets, id);
        Ticket ticket = ticketService.findById(id);
        TicketDTO ticketDTO = mapper.ticketToDTO(ticket);
        List<TicketStatus> statusList = ticketService.findStatusDict();
        model.addAttribute("ticketDTO", ticketDTO);
        model.addAttribute("statusList", statusList);
        return "admin/ticket/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("ticketDTO") TicketDTO ticketDTO, BindingResult result) {
        checkModifiedObjectsConflict(currentlyModifiedTickets, id);
        Ticket ticket = ticketService.findById(id);
        TicketStatus status = ticketService.findStatusById(ticketDTO.getStatus().getId());
        ticket.setStatus(status);
        try {
            ticketService.update(ticket);
            cruiseService.updateCruiseStatusDueToCapacity(ticketService.findById(id).getCruise());
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
        }
        currentlyModifiedTickets.remove(id);
        return REDIRECT_URL;
    }

    @GetMapping("/statusUpdate")
    public String statusUpdate() {
        if (currentlyModifiedTickets.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to force update all ticket statuses: tickets(s) is being modified");
        ticketService.updateAllOutdatedTicketStatuses();
        return REDIRECT_URL;
    }
}
