package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Rental getRental(Long id){
        return rentalRepository.findById(id).get();
    }
}
