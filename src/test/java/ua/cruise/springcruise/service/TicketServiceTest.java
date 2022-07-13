package ua.cruise.springcruise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.*;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.TicketRepository;
import ua.cruise.springcruise.repository.dict.TicketStatusRepository;
import ua.cruise.springcruise.util.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    TicketStatusRepository ticketStatusRepository;

    @Mock
    CruiseRepository cruiseRepository;

    Ticket testTicket1;
    Ticket testTicket2;
    Ticket testTicket3;

    Cruise testCruise1;

    User testUser1;
    User testUser2;

    UserRole testUserRole1;

    TicketStatus testTicketStatus1;
    TicketStatus testTicketStatus2;

    @BeforeEach
    void setUp() {
        testCruise1 = Cruise.builder().id(1L)
                .liner(new Liner(2L, "test liner 1", 3))
                .route(new Route(3L, "test route 1", null))
                .startDateTime(LocalDateTime.MIN)
                .endDateTime(LocalDateTime.MAX)
                .description("CRUISE 1 TEST DESCRIPTION")
                .name("Test cruise 1")
                .imageName("testimg1.jpg")
                .price(BigDecimal.TEN)
                .status(new CruiseStatus(10L, "test status"))
                .build();

        testUserRole1 = new UserRole(6L, "Test Role 1");

        testUser1 = new User(123L, "testLogin", "testPassword", testUserRole1, LocalDateTime.now());
        testUser2 = new User(124L, "testLogin2", "testPassword2", testUserRole1, LocalDateTime.now());

        testTicketStatus1 = new TicketStatus(1L, "Test ticket status 1");
        testTicketStatus2 = new TicketStatus(2L, "Test ticket status 2");

        testTicket1 = new Ticket(1L, testCruise1, testUser1, 2, "docstest1.jpg", BigDecimal.TEN, testTicketStatus1);
        testTicket2 = new Ticket(2L, testCruise1, testUser2, 1, "docstest2.jpg", BigDecimal.TEN, testTicketStatus1);
        testTicket3 = new Ticket(3L, testCruise1, testUser1, 3, "docstest3.jpg", BigDecimal.TEN, testTicketStatus1);

    }

    @Test
    void findAllSuccessCase() {
        List<Ticket> testTicketList = List.of(testTicket1, testTicket2, testTicket3);
        when(ticketRepository.findByOrderByIdAsc()).thenReturn(testTicketList);
        assertEquals(testTicketList, ticketService.findAll());
        verify(ticketRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllEmptyCase() {
        List<Ticket> testTicketList = List.of();
        when(ticketRepository.findByOrderByIdAsc()).thenReturn(testTicketList);
        assertEquals(0, ticketService.findAll().size());
        verify(ticketRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findByIdSuccessCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.ofNullable(testTicket1));
        assertEquals(testTicket1, ticketService.findById(testTicket1.getId()));
        verify(ticketRepository, times(1)).findById(anyLong());

    }

    @Test
    void findByIdNotFoundCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> ticketService.findById(testTicket1.getId()));
        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByUserIdSuccessCase() {
        List<Ticket> testTicketList = List.of(testTicket1, testTicket3);
        when(ticketRepository.findByUser_IdOrderByIdAsc(testUser1.getId())).thenReturn(testTicketList);
        assertEquals(testTicketList, ticketService.findByUserId(testUser1.getId()));
        verify(ticketRepository, times(1)).findByUser_IdOrderByIdAsc(anyLong());
    }

    @Test
    void findByUserIdEmptyCase() {
        List<Ticket> testTicketList = List.of();
        when(ticketRepository.findByUser_IdOrderByIdAsc(testUser1.getId())).thenReturn(testTicketList);
        assertEquals(0, ticketService.findByUserId(testUser1.getId()).size());
        verify(ticketRepository, times(1)).findByUser_IdOrderByIdAsc(anyLong());
    }

    @Test
    void findByCruiseActualSuccessCase() {
        List<Ticket> testTicketList = List.of(testTicket1, testTicket2, testTicket3);
        when(ticketRepository.findByCruiseAndStatus_IdLessThan(testCruise1, Constants.TICKET_ACTUAL_STATUS_LESS_THAN)).thenReturn(testTicketList);
        assertEquals(testTicketList, ticketService.findByCruiseActual(testCruise1));
        verify(ticketRepository, times(1)).findByCruiseAndStatus_IdLessThan(any(Cruise.class), anyLong());
    }

    @Test
    void findByCruiseActualEmptyCase() {
        List<Ticket> testTicketList = List.of();
        when(ticketRepository.findByCruiseAndStatus_IdLessThan(testCruise1, Constants.TICKET_ACTUAL_STATUS_LESS_THAN)).thenReturn(testTicketList);
        assertEquals(0, ticketService.findByCruiseActual(testCruise1).size());
        verify(ticketRepository, times(1)).findByCruiseAndStatus_IdLessThan(any(Cruise.class), anyLong());
    }

    @Test
    void existsSuccessCase() {
        when(ticketRepository.existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(testCruise1, testTicket1.getPosition(), Constants.TICKET_ACTUAL_STATUS_LESS_THAN)).thenReturn(true);
        assertTrue(ticketService.exists(testCruise1, testTicket1.getPosition()));
        verify(ticketRepository, times(1)).existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(any(Cruise.class), anyInt(), anyLong());
    }

    @Test
    void existsNotExistsCase() {
        when(ticketRepository.existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(testCruise1, testTicket1.getPosition(), Constants.TICKET_ACTUAL_STATUS_LESS_THAN)).thenReturn(false);
        assertFalse(ticketService.exists(testCruise1, testTicket1.getPosition()));
        verify(ticketRepository, times(1)).existsByCruiseAndPositionAndStatusIdLessThanAllIgnoreCase(any(Cruise.class), anyInt(), anyLong());
    }

    @Test
    void createSuccessCase() {
        testTicket1.setId(null);
        ticketService.create(testTicket1);
        verify(ticketRepository, times(0)).existsById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));

    }

    @Test
    void createAlreadyExistsCase() {
        when(ticketRepository.existsById(testTicket1.getId())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> ticketService.create(testTicket1));
        verify(ticketRepository, times(1)).existsById(anyLong());
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    @Test
    void updateSuccessCase() {
        when(ticketRepository.existsById(testTicket1.getId())).thenReturn(true);
        ticketService.update(testTicket1);
        verify(ticketRepository, times(1)).existsById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void updateNotExistsCase() {
        when(ticketRepository.existsById(testTicket1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> ticketService.update(testTicket1));
        verify(ticketRepository, times(1)).existsById(anyLong());
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    @Test
    void paySuccessCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.ofNullable(testTicket1));
        when(ticketStatusRepository.findById(Constants.TICKET_PAYED_STATUS)).thenReturn(Optional.ofNullable(testTicketStatus1));
        ticketService.pay(testTicket1.getId());
        verify(ticketStatusRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void payFailedCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.ofNullable(testTicket1));
        when(ticketStatusRepository.findById(Constants.TICKET_PAYED_STATUS)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> ticketService.pay(testTicket1.getId()));
        verify(ticketStatusRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    @Test
    void cancelSuccessCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.ofNullable(testTicket1));
        when(ticketStatusRepository.findById(Constants.TICKET_CANCELED_STATUS)).thenReturn(Optional.ofNullable(testTicketStatus1));
        ticketService.cancel(testTicket1.getId());
        verify(ticketStatusRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void cancelFailedCase() {
        when(ticketRepository.findById(testTicket1.getId())).thenReturn(Optional.ofNullable(testTicket1));
        when(ticketStatusRepository.findById(Constants.TICKET_CANCELED_STATUS)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> ticketService.cancel(testTicket1.getId()));
        verify(ticketStatusRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(0)).save(any(Ticket.class));
    }

    @Test
    void findStatusDictSuccessCase() {
        List<TicketStatus> testTicketStatusList = List.of(testTicketStatus1, testTicketStatus2);
        when(ticketStatusRepository.findByOrderByIdAsc()).thenReturn(testTicketStatusList);
        assertEquals(testTicketStatusList, ticketService.findStatusDict());
        verify(ticketStatusRepository,times(1)).findByOrderByIdAsc();
    }

    @Test
    void findStatusDictEmptyCase() {
        when(ticketStatusRepository.findByOrderByIdAsc()).thenReturn(List.of());
        assertEquals(0, ticketService.findStatusDict().size());
        verify(ticketStatusRepository,times(1)).findByOrderByIdAsc();
    }

    @Test
    void findStatusByIdSuccessCase() {
        when(ticketStatusRepository.findById((long) testTicketStatus1.getId())).thenReturn(Optional.ofNullable(testTicketStatus1));
        assertEquals(testTicketStatus1, ticketService.findStatusById(testTicketStatus1.getId()));
        verify(ticketStatusRepository,times(1)).findById(anyLong());
    }

    @Test
    void findStatusByIdNotFoundCase() {
        when(ticketStatusRepository.findById(testTicketStatus1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> ticketService.findStatusById(testTicketStatus1.getId()));
        verify(ticketStatusRepository,times(1)).findById(anyLong());
    }

    @Test
    void updateAllOutdatedTicketStatusesSuccessCase() {
        when(cruiseRepository.findByStartDateTimeBefore(any(LocalDateTime.class))).thenReturn(List.of(testCruise1));
        when(ticketStatusRepository.findById(Constants.TICKET_OUTDATED_STATUS_ID)).thenReturn(Optional.ofNullable(testTicketStatus1));
        ticketService.updateAllOutdatedTicketStatuses();
        verify(cruiseRepository,times(1)).findByStartDateTimeBefore(any(LocalDateTime.class));
        verify(ticketRepository,times(1)).updateTicketStatusWhereCruise(any(TicketStatus.class), any(List.class));
    }

    @Test
    void updateAllOutdatedTicketStatusesZeroCase() {
        when(cruiseRepository.findByStartDateTimeBefore(any(LocalDateTime.class))).thenReturn(List.of());
        ticketService.updateAllOutdatedTicketStatuses();
        verify(cruiseRepository,times(1)).findByStartDateTimeBefore(any(LocalDateTime.class));
        verify(ticketRepository,times(0)).updateTicketStatusWhereCruise(any(TicketStatus.class), any(List.class));
    }
}