package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.cruise.springcruise.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser_Id(Long id);

    @Query("select (count(t) > 0) from Ticket t " +
            "where t.cruise.id = :id and t.position = :position and t.status.id <= :id1")
    boolean existsByCruise_IdAndPositionAndStatus_IdLessThanEqual(@Param("id") Long id, @Param("position") int position, @Param("id1") Long id1);





}