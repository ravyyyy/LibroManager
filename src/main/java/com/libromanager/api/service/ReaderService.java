package com.libromanager.api.service;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public Reader registerReader(Reader reader) {
        if (readerRepository.existsByEmail(reader.getEmail())) {
            throw new RuntimeException("A reader with this email already exists!");
        }
        return readerRepository.save(reader);
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Reader updateReader(Long id, Reader readerDetails) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reader with id " + id + " was not found"));

        if (!reader.getEmail().equals(readerDetails.getEmail()) &&
            readerRepository.existsByEmail(readerDetails.getEmail())) {
            throw new RuntimeException("Email already in use by another reader!");
        }

        reader.setFirstName(readerDetails.getFirstName());
        reader.setLastName(readerDetails.getLastName());
        reader.setEmail(readerDetails.getEmail());
        reader.setPhoneNumber(readerDetails.getPhoneNumber());

        return readerRepository.save(reader);
    }

    public void deleteReader(Long id) {
        if (!readerRepository.existsById(id)) {
            throw new RuntimeException("Reader with id " + id + " was not found");
        }

        readerRepository.deleteById(id);
    }
}
