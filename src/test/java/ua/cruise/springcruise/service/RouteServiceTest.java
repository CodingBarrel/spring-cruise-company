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
import ua.cruise.springcruise.repository.RouteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RouteServiceTest {

    @InjectMocks
    RouteService routeService;

    @Mock
    RouteRepository routeRepository;

    Route testRoute1;
    Route testRoute2;
    Route testRoute3;

    RoutePoint testRoutePoint1;
    RoutePoint testRoutePoint2;
    RoutePoint testRoutePoint3;

    @BeforeEach
    void setUp() {
        testRoutePoint1 = new RoutePoint(1L, testRoute1, "Test point 1");
        testRoutePoint2 = new RoutePoint(2L, testRoute1, "Test point 2");
        testRoutePoint3 = new RoutePoint(3L, testRoute1, "Test point 3");

        testRoute1 = new Route(1L, "Test route 1", List.of(testRoutePoint1, testRoutePoint2, testRoutePoint3));
        testRoute2 = new Route(1L, "Test route 1", List.of(testRoutePoint1));
        testRoute3 = new Route(1L, "Test route 1", null);
    }


    @Test
    void findAllSuccessCase() {
        List<Route> testRouteList = List.of(testRoute1, testRoute2, testRoute3);
        when(routeRepository.findByOrderByIdAsc()).thenReturn(testRouteList);
        assertEquals(testRouteList, routeService.findAll());
        verify(routeRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllEmptyCase() {
        List<Route> testRouteList = List.of();
        when(routeRepository.findByOrderByIdAsc()).thenReturn(testRouteList);
        assertEquals(0, routeService.findAll().size());
        verify(routeRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void existsByNameSuccessCase() {
        when(routeRepository.existsByName(testRoute1.getName())).thenReturn(true);
        assertTrue(routeService.existsByName(testRoute1.getName()));
        verify(routeRepository, times(1)).existsByName(anyString());
    }

    @Test
    void existsByNameNotFoundCase() {
        when(routeRepository.existsByName(testRoute1.getName())).thenReturn(false);
        assertFalse(routeService.existsByName(testRoute1.getName()));
        verify(routeRepository, times(1)).existsByName(anyString());
    }

    @Test
    void findByIdSuccessCase() {
        when(routeRepository.findById(testRoute1.getId())).thenReturn(Optional.ofNullable(testRoute1));
        assertEquals(testRoute1, routeService.findById(testRoute1.getId()));
        verify(routeRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFoundCase() {
        when(routeRepository.findById(testRoute1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> routeService.findById(testRoute1.getId()));
        verify(routeRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByNameSuccessCase() {
        when(routeRepository.findByName(testRoute1.getName())).thenReturn(Optional.ofNullable(testRoute1));
        assertEquals(testRoute1, routeService.findByName(testRoute1.getName()));
        verify(routeRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByNameNotFoundCase() {
        when(routeRepository.findByName(testRoute1.getName())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> routeService.findByName(testRoute1.getName()));
        verify(routeRepository, times(1)).findByName(anyString());
    }

    @Test
    void updateSuccessCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(true);
        routeService.update(testRoute1);
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    void updateNotExistsCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> routeService.update(testRoute1));
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(0)).save(any(Route.class));
    }

    @Test
    void createSuccessCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(false);
        routeService.create(testRoute1);
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    void createAlreadyExistsCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> routeService.create(testRoute1));
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(0)).save(any(Route.class));
    }

    @Test
    void deleteSuccessCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(true);
        routeService.delete(testRoute1.getId());
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteNotFoundCase() {
        when(routeRepository.existsById(testRoute1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> routeService.delete(testRoute1.getId()));
        verify(routeRepository, times(1)).existsById(anyLong());
        verify(routeRepository, times(0)).deleteById(anyLong());
    }
}