package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Ticket;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

import java.util.Collection;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser_Id(Long id);

    List<Ticket> findByCruiseAndStatus_IdLessThanEqual(Cruise cruise, Long id);

    boolean existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(Cruise cruise, int position, long statusId);

    @Transactional
    @Modifying
    @Query("update Ticket t set t.status = ?1 where t.cruise in ?2")
    int updateTicketStatusWhereCruise(TicketStatus status, Collection<Cruise> cruises);







}