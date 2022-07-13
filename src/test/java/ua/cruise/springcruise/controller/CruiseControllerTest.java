package ua.cruise.springcruise.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.service.CruiseService;
import ua.cruise.springcruise.util.Constants;
import ua.cruise.springcruise.util.EntityMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CruiseControllerTest {

    @InjectMocks
    CruiseController cruiseController;

    @Mock
    CruiseService cruiseService;

    @Mock
    Model model;

    @Test
    void readAllAllValidParametersCase() {
        String dateTimeSignAsString = Constants.equalitySign.GT.toString();
        String dateTimeAsString = LocalDateTime.now().toString();
        String durationSignAsString = Constants.equalitySign.LT.toString();
        Integer duration = 5;
        String durationTypeAsString = Constants.durationType.WEEKS.toString();
        Integer pageId = 2;
        Integer countPerPage = 3;
        when(cruiseService.findByStartDateTimeAndDuration(Constants.equalitySign.GT, LocalDateTime.parse(dateTimeAsString), Constants.equalitySign.LT, duration * 7, 0, 5)).thenReturn(Page.empty());
        assertEquals("cruise/readAll", cruiseController.readAll(dateTimeSignAsString, dateTimeAsString, durationSignAsString, duration, durationTypeAsString, pageId, countPerPage, model));
        verify(model).addAttribute("page", 2);
        verify(model).addAttribute("perPage", 3);
    }

    @Test
    void readAllNoDateTimeCase() {
        String dateTimeSignAsString = null;
        String dateTimeAsString = null;
        String durationSignAsString = Constants.equalitySign.LT.toString();
        Integer duration = 5;
        String durationTypeAsString = Constants.durationType.WEEKS.toString();
        Integer pageId = null;
        Integer countPerPage = null;
        when(cruiseService.findByStartDateTimeAndDuration(null, null, Constants.equalitySign.LT, duration * 7, 0, 5)).thenReturn(Page.empty());
        assertEquals("cruise/readAll", cruiseController.readAll(dateTimeSignAsString, dateTimeAsString, durationSignAsString, duration, durationTypeAsString, pageId, countPerPage, model));
    }

    @Test
    void readAllNoDurationCase() {
        String dateTimeSignAsString = Constants.equalitySign.GT.toString();
        String dateTimeAsString = LocalDateTime.now().toString();
        String durationSignAsString = null;
        Integer duration = null;
        String durationTypeAsString = null;
        Integer pageId = null;
        Integer countPerPage = null;
        when(cruiseService.findByStartDateTimeAndDuration(Constants.equalitySign.GT, LocalDateTime.parse(dateTimeAsString), null, 0, 0, 5)).thenReturn(Page.empty());
        assertEquals("cruise/readAll", cruiseController.readAll(dateTimeSignAsString, dateTimeAsString, durationSignAsString, duration, durationTypeAsString, pageId, countPerPage, model));
    }

    @Test
    void readAllNoParametersCase() {
        String dateTimeSignAsString = null;
        String dateTimeAsString = null;
        String durationSignAsString = null;
        Integer duration = null;
        String durationTypeAsString = null;
        Integer pageId = null;
        Integer countPerPage = null;
        when(cruiseService.findByStartDateTimeAndDuration(null, null, null, 0 * 7, 0, 5)).thenReturn(Page.empty());
        assertEquals("cruise/readAll", cruiseController.readAll(dateTimeSignAsString, dateTimeAsString, durationSignAsString, duration, durationTypeAsString, pageId, countPerPage, model));
    }

    @Test
    void readByIdSuccessCase() {
        Long id = 101L;
        Cruise testCruise1 = Cruise.builder().id(1L)
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
        when(cruiseService.findById(id)).thenReturn(testCruise1);
        assertEquals("cruise/readById", cruiseController.readById(id, model));
        verify(cruiseService,times(1)).findById(anyLong());
        verify(model).addAttribute("cruise", testCruise1);
    }

    @Test
    void readByIdNotFoundCase() {
        Long id = 101L;
        when(cruiseService.findById(id)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        assertThrows(ResponseStatusException.class, () -> cruiseController.readById(id, model));
    }
}