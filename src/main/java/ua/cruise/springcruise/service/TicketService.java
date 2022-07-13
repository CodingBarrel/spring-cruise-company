package ua.cruise.springcruise.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.TicketRepository;
import ua.cruise.springcruise.repository.dict.TicketStatusRepository;
import ua.cruise.springcruise.util.Constants;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A service class that connects controller and model layers, thereby isolating business-logic. Controls requests
 * related to ticket and it's status.
 * @author Vladyslav Kucher
 * @version 1.1
 * @see Service
 * @see Ticket
 */

@Log4j2
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketStatusRepository statusDictRepository;
    private final CruiseRepository cruiseRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketStatusRepository statusDictRepository, CruiseRepository cruiseRepository) {
        this.ticketRepository = ticketRepository;
        this.statusDictRepository = statusDictRepository;
        this.cruiseRepository = cruiseRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findByOrderByIdAsc();
    }

    public Ticket findById(long id) {
        return ticketRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find ticket by id: not exists [id=" + id + "]"));
    }


    public List<Ticket> findByUserId(long id) {
        return ticketRepository.findByUser_IdOrderByIdAsc(id);
    }

    public List<Ticket> findByCruiseActual(Cruise cruise) {
        return ticketRepository.findByCruiseAndStatus_IdLessThan(cruise, Constants.TICKET_ACTUAL_STATUS_LESS_THAN);
    }

    public boolean exists(Cruise cruise, int position) {
        return ticketRepository.existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(cruise, position, Constants.TICKET_ACTUAL_STATUS_LESS_THAN);
    }

    public void create(Ticket ticket) {
        if (ticket.getId() != null && ticketRepository.existsById(ticket.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create ticket: already exists [id=" + ticket.getId() + "]");
        ticketRepository.save(ticket);
    }

    public void update(Ticket ticket) {
        if (!ticketRepository.existsById(ticket.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update ticket: not exists [id=" + ticket.getId() + "]");
        ticketRepository.save(ticket);
    }

    public void pay(long id) {
        Ticket ticket = findById(id);
        ticket.setStatus(statusDictRepository.findById(Constants.TICKET_PAYED_STATUS).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update ticket status to payed: failed to find status [id=" + Constants.TICKET_PAYED_STATUS + "]")));
        ticketRepository.save(ticket);
    }

    public void cancel(long id) {
        Ticket ticket = findById(id);
        ticket.setStatus(statusDictRepository.findById(Constants.TICKET_CANCELED_STATUS).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update ticket status to canceled: failed to find status [id=" + Constants.TICKET_CANCELED_STATUS + "]")));
        ticketRepository.save(ticket);
    }

    public List<TicketStatus> findStatusDict() {
        return statusDictRepository.findByOrderByIdAsc();
    }

    public TicketStatus findStatusById(long id) {
        return statusDictRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to find status by id: not exists [id=" + id + "]"));
    }

    @Scheduled(cron = Constants.TICKET_AUTOUPDATE_DELAY)
    public void updateAllOutdatedTicketStatuses() {
        List<Cruise> cruiseList = cruiseRepository.findByStartDateTimeBefore(LocalDateTime.now());
        if (!cruiseList.isEmpty()) {
            int updatedTicketsCount = ticketRepository.updateTicketStatusWhereCruise(findStatusById(Constants.TICKET_OUTDATED_STATUS_ID), cruiseList);
            log.info("Ticket statuses updated successfully. Result: {} modified as outdated", updatedTicketsCount);
        }
    }

}
