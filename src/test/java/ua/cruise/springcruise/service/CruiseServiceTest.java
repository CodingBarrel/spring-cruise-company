package ua.cruise.springcruise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.repository.CruiseRepository;
import ua.cruise.springcruise.repository.dict.CruiseStatusRepository;
import ua.cruise.springcruise.util.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CruiseServiceTest {

    @InjectMocks
    CruiseService cruiseService;

    @Mock
    CruiseRepository cruiseRepository;

    @Mock
    CruiseStatusRepository cruiseStatusRepository;

    Cruise testCruise1;
    Cruise testCruise2;
    Cruise testCruise3;
    CruiseStatus testStatus1;
    CruiseStatus testStatus2;
    CruiseStatus testStatus3;

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

        testCruise2 = Cruise.builder().id(2L)
                .liner(new Liner(2L, "test liner 1", 3))
                .route(new Route(3L, "test route 1", null))
                .startDateTime(LocalDateTime.MAX)
                .endDateTime(LocalDateTime.MIN)
                .description("CRUISE 2 TEST DESCRIPTION")
                .name("Test cruise 2")
                .imageName("testimg2.jpg")
                .price(BigDecimal.ONE)
                .status(new CruiseStatus(10L, "test status"))
                .build();

        testCruise3 = Cruise.builder().id(3L)
                .liner(new Liner(3L, "test liner 2", 2))
                .route(new Route(2L, "test route 2", null))
                .startDateTime(LocalDateTime.MAX)
                .endDateTime(LocalDateTime.MIN)
                .description("CRUISE 3 TEST DESCRIPTION")
                .name("Test cruise 3")
                .imageName("testimg3.jpg")
                .price(BigDecimal.ONE)
                .status(new CruiseStatus(10L, "test status"))
                .build();

        testStatus1 = new CruiseStatus(1L, "status1");
        testStatus2 = new CruiseStatus(2L, "status2");
        testStatus3 = new CruiseStatus(3L, "status3");
    }

    @Test
    void findAllSuccessCase() {
        List<Cruise> testList = List.of(testCruise1, testCruise2, testCruise3);
        when(cruiseRepository.findByOrderByIdAsc()).thenReturn(testList);
        assertEquals(testList, cruiseService.findAll());
        verify(cruiseRepository, Mockito.times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllEmptyCase() {
        List<Cruise> testList = List.of();
        when(cruiseRepository.findByOrderByIdAsc()).thenReturn(testList);
        assertEquals(testList, cruiseService.findAll());
        verify(cruiseRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findByStartDateTimeAndDurationSuccessCase() {
        Constants.equalitySign dateSign = Constants.equalitySign.E;
        LocalDateTime startDateTime = LocalDateTime.now();
        Constants.equalitySign durationSign = Constants.equalitySign.E;
        int duration = 30;
        int page = 1;
        int size = 5;
        Page<Cruise> testPage = Page.empty();

        when(cruiseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(testPage);
        assertEquals(testPage, cruiseService.findByStartDateTimeAndDuration(dateSign, startDateTime, durationSign, duration, page, size));
        verify(cruiseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findByStartDateTimeAndDurationEmptyCase() {
        Constants.equalitySign dateSign = null;
        LocalDateTime startDateTime = null;
        Constants.equalitySign durationSign = null;
        int duration = 0;
        int page = 1;
        int size = 5;
        Page<Cruise> testPage = Page.empty();

        when(cruiseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(testPage);
        assertEquals(testPage, cruiseService.findByStartDateTimeAndDuration(dateSign, startDateTime, durationSign, duration, page, size));
        verify(cruiseRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findByNameSuccessCase() {
        when(cruiseRepository.findByName(testCruise1.getName())).thenReturn(Optional.ofNullable(testCruise1));
        assertEquals(testCruise1, cruiseService.findByName(testCruise1.getName()));
        verify(cruiseRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByNameNotFoundCase() {
        when(cruiseRepository.findByName(testCruise1.getName())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> cruiseService.findByName(testCruise1.getName()));
        verify(cruiseRepository, times(1)).findByName(anyString());
    }

    @Test
    void existsByNameSuccessCase() {
        when(cruiseRepository.existsByName(anyString())).thenReturn(true);
        assertTrue(cruiseService.existsByName("myName1"));
        verify(cruiseRepository, times(1)).existsByName(anyString());
    }

    @Test
    void existsByNameNotExistsCase() {
        when(cruiseRepository.existsByName("myName1")).thenReturn(false);
        assertFalse(cruiseService.existsByName("myName1"));
        verify(cruiseRepository, times(1)).existsByName(anyString());
    }

    @Test
    void findByIdSuccessCase() {
        when(cruiseRepository.findById(testCruise2.getId())).thenReturn(Optional.ofNullable(testCruise2));
        assertEquals(testCruise2, cruiseService.findById(testCruise2.getId()));
        verify(cruiseRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFoundCase() {
        when(cruiseRepository.findById(testCruise2.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> cruiseService.findById(testCruise2.getId()));
        verify(cruiseRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateSuccessCase() {
        when(cruiseRepository.existsById(testCruise3.getId())).thenReturn(true);
        cruiseService.update(testCruise3);
        verify(cruiseRepository, times(1)).existsById(testCruise3.getId());
        verify(cruiseRepository, times(1)).save(any(Cruise.class));
    }

    @Test
    void updateNotExistsCase() {
        when(cruiseRepository.existsById(testCruise3.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> cruiseService.update(testCruise3));
        verify(cruiseRepository, times(1)).existsById(testCruise3.getId());
        verify(cruiseRepository, times(0)).save(any(Cruise.class));
    }


    @Test
    void createSuccessCase() {
        testCruise3.setId(null);
        cruiseService.create(testCruise3);
        verify(cruiseRepository, times(1)).save(any(Cruise.class));
    }

    @Test
    void createFailedCase() {
        when(cruiseRepository.existsById(testCruise3.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> cruiseService.update(testCruise3));
        verify(cruiseRepository, times(0)).save(any(Cruise.class));
    }

    @Test
    void findAllStatusesSuccessCase() {
        List<CruiseStatus> testStatusList = List.of(testStatus1, testStatus2, testStatus3);
        when(cruiseStatusRepository.findByOrderByIdAsc()).thenReturn(testStatusList);
        assertEquals(testStatusList, cruiseService.findAllStatuses());
        verify(cruiseStatusRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllStatusesEmptyCase() {
        List<CruiseStatus> testStatusList = List.of();
        when(cruiseStatusRepository.findByOrderByIdAsc()).thenReturn(testStatusList);
        assertEquals(testStatusList, cruiseService.findAllStatuses());
        verify(cruiseStatusRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findStatusByIdSuccessCase() {
        when(cruiseStatusRepository.findById((long) testStatus1.getId())).thenReturn(Optional.ofNullable(testStatus1));
        assertEquals(testStatus1, cruiseService.findStatusById(testStatus1.getId()));
        verify(cruiseStatusRepository, times(1)).findById(anyLong());
    }

    @Test
    void findStatusByIdNotFoundCase() {
        when(cruiseStatusRepository.findById(testStatus1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> cruiseService.findStatusById(testStatus1.getId()));
        verify(cruiseStatusRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteSuccessCase() {
        when(cruiseRepository.existsById(testCruise1.getId())).thenReturn(true);
        cruiseService.delete(testCruise1.getId());
        verify(cruiseRepository, times(1)).existsById(testCruise1.getId());
        verify(cruiseRepository, times(1)).deleteById(testCruise1.getId());
    }

    @Test
    void deleteNotFoundCase() {
        when(cruiseRepository.existsById(testCruise1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> cruiseService.delete(testCruise1.getId()));
        verify(cruiseRepository, times(1)).existsById(testCruise1.getId());
        verify(cruiseRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void updateCruiseStatusDueToCapacitySuccessToFullCase() {
        testStatus1.setId(Constants.CRUISE_ACTUAL_STATUS);
        testCruise2.setStatus(testStatus1);
        when(cruiseRepository.countByIdAndTicketList_Status_IdLessThan(testCruise2.getId(), Constants.TICKET_ACTUAL_STATUS_LESS_THAN)).thenReturn((long) testCruise2.getLiner().getPassengerCapacity());
        when(cruiseStatusRepository.findById(Constants.CRUISE_FULL_STATUS)).thenReturn(Optional.ofNullable(testStatus1));
        when(cruiseRepository.existsById(testCruise2.getId())).thenReturn(true);
        cruiseService.updateCruiseStatusDueToCapacity(testCruise2);
        verify(cruiseRepository, times(1)).save(any(Cruise.class));
    }

    @Test
    void updateCruiseStatusDueToCapacitySuccessToActualCase() {
        testStatus1.setId(Constants.CRUISE_FULL_STATUS);
        testCruise2.setStatus(testStatus1);
        when(cruiseRepository.countByIdAndTicketList_Status_IdLessThan(testCruise2.getId(), testStatus1.getId())).thenReturn(0L);
        when(cruiseStatusRepository.findById(Constants.CRUISE_ACTUAL_STATUS)).thenReturn(Optional.ofNullable(testStatus1));
        when(cruiseRepository.existsById(testCruise2.getId())).thenReturn(true);
        cruiseService.updateCruiseStatusDueToCapacity(testCruise2);
        verify(cruiseRepository, times(1)).save(any(Cruise.class));
    }
}