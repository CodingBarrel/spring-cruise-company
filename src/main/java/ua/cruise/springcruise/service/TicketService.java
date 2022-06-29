package ua.cruise.springcruise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.dictionary.TicketStatusDict;
import ua.cruise.springcruise.repository.TicketRepository;
import ua.cruise.springcruise.repository.dict.TicketStatusDictRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketStatusDictRepository statusDictRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketStatusDictRepository statusDictRepository) {
        this.ticketRepository = ticketRepository;
        this.statusDictRepository = statusDictRepository;
    }

    public List<Ticket> findAll(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(long id){
        return ticketRepository.findById(id);
    }

    public List<Ticket> findByUserId(long id){
        return ticketRepository.findByUser_Id(id);
    }

    @Transactional
    public void create(Ticket ticket){
        if (!ticketRepository.existsByCruise_IdAndPositionAndStatus_IdLessThanEqual(ticket.getCruise().getId(), ticket.getPosition(), 3L))
            ticketRepository.save(ticket);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket already exists");
    }

    @Transactional
    public void update(Ticket ticket){
        if (ticketRepository.existsById(ticket.getId()))
            ticketRepository.save(ticket);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found");
    }

    public void pay(long id){
            Ticket ticket = findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found"));
            ticket.setStatus(statusDictRepository.findById(3L).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
            ticketRepository.save(ticket);
    }

    public void cancel(long id){
        Ticket ticket = findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found"));
        ticket.setStatus(statusDictRepository.findById(5L).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)));
        ticketRepository.save(ticket);
    }

    public List<TicketStatusDict> findStatusDict(){
        return statusDictRepository.findAll();
    }

}
