package com.libromanager.api.service;

import com.libromanager.api.entity.Reader;
import com.libromanager.api.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return readerRepository.findAll();
    }
}
