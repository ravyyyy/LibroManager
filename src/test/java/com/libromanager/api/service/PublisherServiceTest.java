package com.libromanager.api.service;

import com.libromanager.api.entity.Publisher;
import com.libromanager.api.repository.PublisherRepository;
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
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        publisherService = new PublisherService(publisherRepository);
    }

    @Test
    void testGetAllPublishers() {
        when(publisherRepository.findAll(any(Sort.class))).thenReturn(List.of(new Publisher()));
        List<Publisher> result = publisherService.getAllPublishers();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdatePublisher() {
        Long id = 1L;
        Publisher existing = new Publisher(id, "Old Name", "Old Address", null);
        Publisher updateInfo = new Publisher();
        updateInfo.setName("New Name");
        updateInfo.setAddress("New Address");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(existing));
        when(publisherRepository.save(any())).thenReturn(existing);

        Publisher result = publisherService.updatePublisher(id, updateInfo);
        assertEquals("New Name", result.getName());
    }

    @Test
    void testDeletePublisher() {
        when(publisherRepository.existsById(1L)).thenReturn(true);
        publisherService.deletePublisher(1L);
        verify(publisherRepository).deleteById(1L);
    }
}