package com.libromanager.api.service;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        Reader r1 = new Reader();
        r1.setFirstName("A");
        Reader r2 = new Reader();
        r2.setFirstName("B");

        when(readerRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Reader> results = readerService.getAllReaders();

        assertEquals(2, results.size());
    }
}