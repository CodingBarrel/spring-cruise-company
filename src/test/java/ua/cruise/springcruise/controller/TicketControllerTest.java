package ua.cruise.springcruise.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.dto.TicketDTO;
import ua.cruise.springcruise.entity.*;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.service.StorageService;
import ua.cruise.springcruise.service.TicketService;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapperImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TicketControllerTest {

    @InjectMocks
    TicketController ticketController;

    @Mock
    EntityMapperImpl mapper;

    @Mock
    TicketService ticketService;

    @Mock
    CruiseService cruiseService;

    @Mock
    StorageService storageService;

    @Mock
    Model model;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Mock
    MultipartFile file;

    User testUser;

    UserRole testUserRole;

    TicketStatus ticketStatus1;
    TicketStatus ticketStatus2;

    Ticket testTicket1;
    Ticket testTicket2;
    Ticket testTicket3;

    Cruise testCruise;


    @BeforeEach
    void setUp() {
        testUserRole = new UserRole(1L, "Test user role 1");
        testUser = new User(1L, "login", "password", testUserRole, LocalDateTime.now());

        testCruise = Cruise.builder().id(1L)
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

        ticketStatus1 = new TicketStatus(1L, "Test status 1");
        ticketStatus2 = new TicketStatus(2L, "Test status 2");

        testTicket1 = new Ticket(1L, testCruise, testUser, 1, "img1.jpg", BigDecimal.ONE, ticketStatus1);
        testTicket2 = new Ticket(2L, testCruise, testUser, 2, "img2.jpg", BigDecimal.ONE, ticketStatus1);
        testTicket3 = new Ticket(3L, testCruise, testUser, 3, "img1.jpg", BigDecimal.ONE, ticketStatus2);
    }

    @Test
    void readByUserIdSuccessCase() {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            List<Ticket> testTicketList = List.of(testTicket1, testTicket2, testTicket3);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findByUserId(testUser.getId())).thenReturn(testTicketList);
            assertEquals("ticket/readMy", ticketController.readByUserId(model));
            verify(ticketService, times(1)).findByUserId(anyLong());
            verify(model).addAttribute("ticketList", testTicketList);
        }
    }

    @Test
    void readByUserIdEmptyCase() {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findByUserId(testUser.getId())).thenReturn(List.of());
            assertEquals("ticket/readMy", ticketController.readByUserId(model));
            verify(ticketService, times(1)).findByUserId(anyLong());
            verify(model).addAttribute("ticketList", List.of());
        }
    }

    @Test
    void createFormSuccessCase() {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            Long id = 12345L;
            List<Ticket> testTicketList = List.of(testTicket1, testTicket2, testTicket3);
            when(cruiseService.findById(id)).thenReturn(testCruise);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findByCruiseActual(testCruise)).thenReturn(testTicketList);
            assertEquals("ticket/create", ticketController.createForm(id, model));
            verify(ticketService, times(1)).findByCruiseActual(any(Cruise.class));
            verify(model).addAttribute("ticketList", testTicketList);
        }
    }

    @Test
    void createFormEmptyTicketsCase() {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            Long id = 12345L;
            List<Ticket> testTicketList = List.of();
            when(cruiseService.findById(id)).thenReturn(testCruise);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findByCruiseActual(testCruise)).thenReturn(testTicketList);
            assertEquals("ticket/create", ticketController.createForm(id, model));
            verify(ticketService, times(1)).findByCruiseActual(any(Cruise.class));
            verify(model).addAttribute("ticketList", testTicketList);
        }
    }

    @Test
    void createSuccessCase() throws IOException {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            TicketDTO ticketDTO = new TicketDTO(123L, testCruise, testUser, 10, "testdocs1.png", BigDecimal.TEN, ticketStatus1);
            when(file.getOriginalFilename()).thenReturn("testdocs1.png");
            when(mapper.dtoToTicket(any(TicketDTO.class))).thenCallRealMethod();
            when(cruiseService.findById(testCruise.getId())).thenReturn(testCruise);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID)).thenReturn(ticketStatus1);
            when(ticketService.exists(testCruise, testTicket1.getPosition())).thenReturn(false);
            when(storageService.save(any(Path.class), anyString(), any(MultipartFile.class))).thenReturn("qwerty123.png");
            assertEquals("redirect:/ticket", ticketController.create(testCruise.getId(), ticketDTO, file));
            verify(ticketService, times(1)).create(any(Ticket.class));
            verify(cruiseService, times(1)).updateCruiseStatusDueToCapacity(any(Cruise.class));
        }
    }

    @Test
    void createAlreadyExistsCase() throws IOException {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            TicketDTO ticketDTO = new TicketDTO(123L, testCruise, testUser, 10, "testdocs1.png", BigDecimal.TEN, ticketStatus1);
            when(file.getOriginalFilename()).thenReturn("testdocs1.png");
            when(mapper.dtoToTicket(any(TicketDTO.class))).thenCallRealMethod();
            when(cruiseService.findById(testCruise.getId())).thenReturn(testCruise);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID)).thenReturn(ticketStatus1);
            when(ticketService.exists(testCruise, ticketDTO.getPosition())).thenReturn(true);
            assertThrows(ResponseStatusException.class, () -> ticketController.create(testCruise.getId(), ticketDTO, file));
            verify(ticketService, times(0)).create(any(Ticket.class));
            verify(cruiseService, times(0)).updateCruiseStatusDueToCapacity(any(Cruise.class));
        }
    }

    @Test
    void createFailedToSaveFileCase() throws IOException {
        try (MockedStatic<SecurityContextHolder> mockedSCH = Mockito.mockStatic(SecurityContextHolder.class)) {
            TicketDTO ticketDTO = new TicketDTO(123L, testCruise, testUser, 10, "testdocs1.png", BigDecimal.TEN, ticketStatus1);
            when(file.getOriginalFilename()).thenReturn("testdocs1.png");
            when(mapper.dtoToTicket(any(TicketDTO.class))).thenCallRealMethod();
            when(cruiseService.findById(testCruise.getId())).thenReturn(testCruise);
            mockedSCH.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(ticketService.findStatusById(Constants.TICKET_DEFAULT_STATUS_ID)).thenReturn(ticketStatus1);
            when(ticketService.exists(testCruise, testTicket1.getPosition())).thenReturn(true);
            when(storageService.save(any(Path.class), anyString(), any(MultipartFile.class))).thenThrow(new IOException());
            assertThrows(ResponseStatusException.class, () -> ticketController.create(testCruise.getId(), ticketDTO, file));
            verify(ticketService, times(0)).create(any(Ticket.class));
            verify(cruiseService, times(0)).updateCruiseStatusDueToCapacity(any(Cruise.class));
        }
    }


    @Test
    void paySuccessCase() {
        Long id = 123123L;
        when(ticketService.findById(id)).thenReturn(testTicket1);
        ticketController.changeStatus(id, Constants.TICKET_PAYED_STATUS);
        verify(ticketService, times(1)).pay(anyLong());
    }

    @Test
    void cancelSuccessCase() {
        Long id = 123123L;
        when(ticketService.findById(id)).thenReturn(testTicket1);
        ticketController.changeStatus(id, Constants.TICKET_CANCELED_STATUS);
        verify(ticketService, times(1)).cancel(anyLong());
    }
}