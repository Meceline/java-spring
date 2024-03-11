package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.FormData;
import com.openclassrooms.javaspring.dto.MessageResponse;
import com.openclassrooms.javaspring.dto.RentalDto;
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
             //convert rental to dto : string to MultipartFile

             //rentalDto.setPicture(r.getPicture());
             rentalDto.setDescription(r.getDescription());
             rentalDto.setOwner_id(r.getOwner().getId());
             rentalDto.setCreated_at(r.getCreated_at());
             rentalDto.setUpdated_at(r.getUpdated_at());
             list.add(rentalDto);
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


/*    @PostMapping("/rental")
    public MessageResponse createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") int surface,
            @RequestParam("price") int price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture

    ) {
        System.out.print("O");
        User u = new User();
        u.setId((long) 1);

        System.out.println("1");
    *//*    String pictureUrl = fileUpload.uploadFile(picture);*//*

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setCreated_at(new Date());
        rental.setUpdated_at(new Date());
        rental.setOwner(u);
       *//* rental.setPicture(pictureUrl);*//*

        System.out.println("2");
        rentalService.createRental(rental);
        System.out.println("3");
        MessageResponse response = new MessageResponse();
        response.setMessage("Rental created !");
        return response;
    }*/


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
