package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.*;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.service.FileUpload;
import com.openclassrooms.javaspring.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("/rental/{id}")
    public RentalResponse getRental(@PathVariable("id") final String id_string){
        long id = Long.parseLong(id_string);
        Rental r = rentalService.getRental(id).get();

        RentalResponse rentalResponse = new RentalResponse();
        rentalResponse.setId(r.getId());
        rentalResponse.setName(r.getName());
        rentalResponse.setSurface(r.getSurface());
        rentalResponse.setPrice(r.getPrice());
        rentalResponse.setPicture(r.getPicture());
        rentalResponse.setDescription(r.getDescription());
        rentalResponse.setOwner_id(r.getOwner().getId());
        rentalResponse.setCreated_at(r.getCreated_at());
        rentalResponse.setUpdated_at(r.getUpdated_at());

        return rentalResponse;
    }

    @GetMapping("/rentals")
    public Iterable<RentalResponse> getRentals(){
         Iterable<Rental> rentals = rentalService.getRentals();
         List<RentalResponse> list = new ArrayList<RentalResponse>();
         for(Rental r: rentals){
             RentalResponse rentalResponse = new RentalResponse();
             rentalResponse.setId(r.getId());
             rentalResponse.setName(r.getName());
             rentalResponse.setSurface(r.getSurface());
             rentalResponse.setPrice(r.getPrice());
             rentalResponse.setPicture(r.getPicture());
             rentalResponse.setDescription(r.getDescription());
             rentalResponse.setOwner_id(r.getOwner().getId());
             rentalResponse.setCreated_at(r.getCreated_at());
             rentalResponse.setUpdated_at(r.getUpdated_at());
             list.add(rentalResponse);
         }
        return list;
    }

    @PostMapping("/rental")
    public MessageResponse createRental(@RequestParam("name") String name,
                                        @RequestParam("surface") int surface,
                                        @RequestParam("price") int price,
                                        @RequestParam("description") String description,
                                        @RequestParam("picture") MultipartFile picture) throws IOException {

        String pictureUrl = fileUpload.uploadFile(picture);

        User u = new User();
        u.setId((long) 1);
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setCreated_at(new Date());
        rental.setUpdated_at(new Date());
        rental.setOwner(u);
        rental.setPicture(pictureUrl);

        rentalService.createRental(rental);
        MessageResponse response = new MessageResponse();
        response.setMessage("Rental created !");


        return response;
    }

    @PutMapping("/rental/{id}")
    public MessageResponse updateRental(@PathVariable("id") final String id_string, @RequestBody FormData formData) {
        System.out.print("O");
        long id = Long.parseLong(id_string);
        Rental r = rentalService.getRental(id).get();
        User u = new User();
        u.setId((long) 1);

        Rental rental = new Rental();
        rental.setId(id);
        rental.setOwner(u);
        rental.setPicture(r.getPicture());
        rental.setName(formData.getName());
        rental.setSurface(formData.getSurface());
        rental.setPrice(formData.getPrice());
        rental.setDescription(formData.getDescription());
        rental.setUpdated_at(new Date());

        rentalService.updateRental(rental);

        MessageResponse response = new MessageResponse();
        response.setMessage("Rental updated !");
        System.out.print("1");
        return response;
    }

}
