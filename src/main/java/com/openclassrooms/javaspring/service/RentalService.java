package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> getRental(Long id){
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals(){
        return rentalRepository.findAll();
    }

    public void createRental(Rental r){
        rentalRepository.save(r);
    }

    public void updateRental(Rental rental){
        rentalRepository.save((rental));
    }
}
