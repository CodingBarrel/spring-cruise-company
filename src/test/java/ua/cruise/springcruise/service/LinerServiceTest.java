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
import ua.cruise.springcruise.repository.LinerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class LinerServiceTest {

    @InjectMocks
    LinerService linerService;

    @Mock
    LinerRepository linerRepository;

    Liner testLiner1;
    Liner testLiner2;
    Liner testLiner3;

    @BeforeEach
    void setUp() {
        testLiner1 = new Liner(1L, "Test liner 1", 50);
        testLiner2 = new Liner(2L, "Test liner 2", 51);
        testLiner3 = new Liner(3L, "Test liner 3", 52);
    }

    @Test
    void findAllSuccessCase() {
        List<Liner> testLinerList = List.of(testLiner1, testLiner2, testLiner3);
        when(linerRepository.findByOrderByIdAsc()).thenReturn(testLinerList);
        assertEquals(testLinerList, linerService.findAll());
        verify(linerRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findAllEmptyCase() {
        when(linerRepository.findByOrderByIdAsc()).thenReturn(List.of());
        assertEquals(0, linerService.findAll().size());
        verify(linerRepository, times(1)).findByOrderByIdAsc();
    }

    @Test
    void findByIdSuccessCase() {
        when(linerRepository.findById(testLiner1.getId())).thenReturn(Optional.ofNullable(testLiner1));
        assertEquals(testLiner1, linerService.findById(testLiner1.getId()));
        verify(linerRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFoundCase() {
        when(linerRepository.findById(testLiner1.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> linerService.findById(testLiner1.getId()));
        verify(linerRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByNameSuccessCase() {
        when(linerRepository.findByName(testLiner1.getName())).thenReturn(Optional.ofNullable(testLiner1));
        assertEquals(testLiner1, linerService.findByName(testLiner1.getName()));
        verify(linerRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByNameNotFoundCase() {
        when(linerRepository.findByName(testLiner1.getName())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> linerService.findByName(testLiner1.getName()));
        verify(linerRepository, times(1)).findByName(anyString());
    }

    @Test
    void existsByNameSuccessCase() {
        when(linerRepository.existsByName(testLiner1.getName())).thenReturn(true);
        assertTrue(linerService.existsByName(testLiner1.getName()));
        verify(linerRepository, times(1)).existsByName(anyString());
    }

    @Test
    void existsByNameNotFoundCase() {
        when(linerRepository.existsByName(testLiner1.getName())).thenReturn(false);
        assertFalse(linerService.existsByName(testLiner1.getName()));
        verify(linerRepository, times(1)).existsByName(anyString());
    }

    @Test
    void updateSuccessCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(true);
        linerService.update(testLiner1);
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(1)).save(any(Liner.class));
    }

    @Test
    void updateNotExistsCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> linerService.update(testLiner1));
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(0)).save(any(Liner.class));
    }

    @Test
    void createSuccessCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(false);
        linerService.create(testLiner1);
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(1)).save(any(Liner.class));
    }

    @Test
    void createAlreadyExistsCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> linerService.create(testLiner1));
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(0)).save(any(Liner.class));
    }

    @Test
    void deleteSuccessCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(true);
        linerService.delete(testLiner1.getId());
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteNotExistsCase() {
        when(linerRepository.existsById(testLiner1.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> linerService.delete(testLiner1.getId()));
        verify(linerRepository, times(1)).existsById(anyLong());
        verify(linerRepository, times(0)).deleteById(anyLong());
    }
}