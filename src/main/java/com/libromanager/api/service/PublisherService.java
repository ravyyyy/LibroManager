package com.libromanager.api.service;

import com.libromanager.api.entity.Publisher;
import com.libromanager.api.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher addPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Publisher updatePublisher(Long id, Publisher publisherDetails) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher with id " + id + " was not found"));

        publisher.setName(publisherDetails.getName());
        publisher.setAddress(publisherDetails.getAddress());

        return publisherRepository.save(publisher);
    }

    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new RuntimeException("Publisher with id " + id + " was not found");
        }

        publisherRepository.deleteById(id);
    }
}
