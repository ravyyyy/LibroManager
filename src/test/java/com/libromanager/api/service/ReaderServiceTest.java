package com.libromanager.api.service;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @Mock
    private ReaderRepository readerRepository;

    private ReaderService readerService;

    @BeforeEach
    void setUp() {
        readerService = new ReaderService(readerRepository);
    }

    @Test
    void testRegisterReader_Success() {
        Reader newReader = new Reader();
        newReader.setFirstName("Ion");
        newReader.setEmail("ion@test.com");

        when(readerRepository.existsByEmail("ion@test.com")).thenReturn(false);

        Reader savedReader = new Reader();
        savedReader.setId(1L);
        savedReader.setEmail("ion@test.com");

        when(readerRepository.save(any(Reader.class))).thenReturn(savedReader);

        Reader result = readerService.registerReader(newReader);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(readerRepository, times(1)).save(any(Reader.class));
    }

    @Test
    void testRegisterReader_EmailAlreadyExists() {
        Reader newReader = new Reader();
        newReader.setEmail("existent@test.com");

        when(readerRepository.existsByEmail("existent@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            readerService.registerReader(newReader);
        });

        assertEquals("A reader with this email already exists!", exception.getMessage());
        verify(readerRepository, never()).save(any());
    }

    @Test
    void testGetAllReaders() {
        when(readerRepository.findAll(any(Sort.class))).thenReturn(List.of(new Reader()));
        assertEquals(1, readerService.getAllReaders().size());
    }

    @Test
    void testUpdateReader() {
        Long id = 1L;
        Reader existing = new Reader();
        existing.setId(id);
        existing.setEmail("old@test.com");

        Reader updateDetails = new Reader();
        updateDetails.setEmail("new@test.com");

        when(readerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(readerRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(readerRepository.save(any())).thenReturn(existing);

        Reader result = readerService.updateReader(id, updateDetails);
        assertEquals("new@test.com", result.getEmail());
    }

    @Test
    void testDeleteReader() {
        when(readerRepository.existsById(1L)).thenReturn(true);
        readerService.deleteReader(1L);
        verify(readerRepository).deleteById(1L);
    }
}