package ua.cruise.springcruise.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.repository.LinerRepository;
import ua.cruise.springcruise.repository.RoutePointRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RoutePointServiceTest {

    @InjectMocks
    RoutePointService routePointService;

    @Mock
    RoutePointRepository routePointRepository;

    RoutePoint testRoutePoint1;
    RoutePoint testRoutePoint2;
    RoutePoint testRoutePoint3;

    Route route;

    @BeforeEach
    void setUp() {
        testRoutePoint1 = new RoutePoint(1L, route, "Test point 1");
        testRoutePoint2 = new RoutePoint(2L, route, "Test point 2");
        testRoutePoint3 = new RoutePoint(3L, route, "Test point 3");
        route = new Route(10L, "Test route 1", List.of(testRoutePoint1, testRoutePoint2, testRoutePoint3));
    }


    @Test
    void findByRouteIdSuccessCase() {
        when(routePointRepository.findByRouteId(route.getId())).thenReturn(route.getRoutePointList());
        assertEquals(route.getRoutePointList(), routePointService.findByRouteId(route.getId()));
        verify(routePointRepository, times(1)).findByRouteId(anyLong());
    }

    @Test
    void findByRouteIdEmptyCase() {
        when(routePointRepository.findByRouteId(route.getId())).thenReturn(List.of());
        assertEquals(0, routePointService.findByRouteId(route.getId()).size());
        verify(routePointRepository, times(1)).findByRouteId(anyLong());
    }

    @Test
    void findByIdSuccessCase() {
        when(routePointRepository.findById(testRoutePoint1.getId())).thenReturn(Optional.ofNullable(testRoutePoint1));
        assertEquals(testRoutePoint1, routePointService.findById(testRoutePoint1.getId()));
        verify(routePointRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFoundCase() {
        when(routePointRepository.findById(testRoutePoint1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> routePointService.findById(testRoutePoint1.getId()));
        verify(routePointRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByNameSuccessCase() {
        when(routePointRepository.findByName(testRoutePoint1.getName())).thenReturn(Optional.ofNullable(testRoutePoint1));
        assertEquals(testRoutePoint1, routePointService.findByName(testRoutePoint1.getName()));
        verify(routePointRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByNameNotFoundCase() {
        when(routePointRepository.findByName(testRoutePoint1.getName())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> routePointService.findByName(testRoutePoint1.getName()));
        verify(routePointRepository, times(1)).findByName(anyString());
    }

    @Test
    void existsByNameSuccessCase() {
        when(routePointRepository.existsByName(testRoutePoint1.getName())).thenReturn(true);
        assertTrue(routePointService.existsByName(testRoutePoint1.getName()));
        verify(routePointRepository, times(1)).existsByName(anyString());
    }


    @Test
    void existsByNameNotFoundCase() {
        when(routePointRepository.existsByName(testRoutePoint1.getName())).thenReturn(false);
        assertFalse(routePointService.existsByName(testRoutePoint1.getName()));
        verify(routePointRepository, times(1)).existsByName(anyString());
    }

    @Test
    void createSuccessCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(false);
        routePointService.create(testRoutePoint1);
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(1)).save(any(RoutePoint.class));
    }

    @Test
    void createAlreadyExistsCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> routePointService.create(testRoutePoint1));
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(0)).save(any(RoutePoint.class));
    }

    @Test
    void updateSuccessCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(true);
        routePointService.update(testRoutePoint1);
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(1)).save(any(RoutePoint.class));
    }

    @Test
    void updateNotExistsCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> routePointService.update(testRoutePoint1));
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(0)).save(any(RoutePoint.class));
    }

    @Test
    void deleteSuccessCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(true);
        routePointService.delete(testRoutePoint1.getId());
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteNotExistsCase() {
        when(routePointRepository.existsById(testRoutePoint1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> routePointService.delete(testRoutePoint1.getId()));
        verify(routePointRepository, times(1)).existsById(anyLong());
        verify(routePointRepository, times(0)).deleteById(anyLong());
    }
}