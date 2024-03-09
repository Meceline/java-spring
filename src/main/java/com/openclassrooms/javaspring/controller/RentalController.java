package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.MessageResponse;
import com.openclassrooms.javaspring.dto.RentalDto;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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

    @PostMapping("/rental")
    public MessageResponse createRetal(@RequestBody RentalDto rentalDto) {
        //TODO Récupérer le user courant  - session ?
        Rental rental = new Rental();
        rental.setName(rentalDto.getName());
        rental.setSurface(rentalDto.getSurface());
        rental.setPrice(rentalDto.getPrice());
        rental.setPicture(rentalDto.getPicture());
        rental.setDescription(rentalDto.getDescription());
        rental.setCreated_at(new Date());
        rental.setUpdated_at(new Date());
User u = new User();
u.setId((long) 1);
rental.setOwner(u);

        rentalService.createRental(rental);

        MessageResponse response = new MessageResponse();
        response.setMessage("Rental created !");
        return response;
    }


}
