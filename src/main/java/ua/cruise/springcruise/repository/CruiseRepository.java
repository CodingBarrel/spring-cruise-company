package ua.cruise.springcruise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CruiseRepository extends JpaRepository<Cruise, Long>, JpaSpecificationExecutor<Cruise> {

    List<Cruise> findByOrderByIdAsc();

    Optional<Cruise> findByName(String name);

    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("update Cruise c set c.status = ?1 where c.startDateTime < ?2")
    int updateStartedCruises(CruiseStatus status, LocalDateTime startDateTime);

    @Transactional
    @Modifying
    @Query("update Cruise c set c.status = ?1 where c.endDateTime < ?2")
    int updateEndedCruises(CruiseStatus status, LocalDateTime endDateTime);

    List<Cruise> findByStartDateTimeBefore(LocalDateTime startDateTime);

    long countByIdAndTicketList_Status_IdLessThan(Long cruiseId, Long statusId);

}