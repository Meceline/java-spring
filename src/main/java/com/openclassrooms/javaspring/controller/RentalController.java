package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @GetMapping("/rental/{id}")
    public Rental getRental(@PathVariable("id") final String id_string){
        long id = Long.parseLong(id_string);
        Rental rental = rentalService.getRental(id);
        return rental;
    }

}
