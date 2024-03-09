package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.RentalDto;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @GetMapping("/rental/{id}")
    public Optional<Rental> getRental(@PathVariable("id") final String id_string){
        long id = Long.parseLong(id_string);
        Optional<Rental> rental = rentalService.getRental(id);
        return rental;
    }

    @GetMapping("/rentals")
    public Iterable<RentalDto> getRentals(){
         Iterable<Rental> rentals = rentalService.getRentals();
         List<RentalDto> list = new ArrayList<RentalDto>();
         for(Rental r: rentals){
             RentalDto rentalDto = new RentalDto();
             rentalDto.setId(r.getId());
             rentalDto.setName(r.getName());
             rentalDto.setSurface(r.getSurface());
             rentalDto.setPrice(r.getPrice());
             rentalDto.setPicture(r.getPicture());
             rentalDto.setDescription(r.getDescription());
             rentalDto.setOwner_id(r.getOwner().getId());
             rentalDto.setCreated_at(r.getCreated_at());
             rentalDto.setUpdated_at(r.getUpdated_at());
             list.add(rentalDto);
         }
        return list;
    }

}
