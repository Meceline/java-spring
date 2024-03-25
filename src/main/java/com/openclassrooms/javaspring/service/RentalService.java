package com.openclassrooms.javaspring.service;

import com.openclassrooms.javaspring.dto.UserResponse;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.io.IOException;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;


@Service
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private FileUpload fileUpload;

    public Optional<Rental> getRental(Long id){
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals(){
        return rentalRepository.findAll();
    }

    public void createRental(String name, int surface, int price, String description, MultipartFile picture, UserResponse user) throws IOException {
        // Validation des champs
        if (name.isEmpty() || surface <= 0 || price <= 0 || description.isEmpty()) {
            throw new IllegalArgumentException("Name, surface, price, and description are required fields");
        }

        // Transfert du fichier
        String pictureUrl = fileUpload.uploadFile(picture);

        // Préparation du rental
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setCreated_at(new Date());
        rental.setUpdated_at(new Date());

        User u = new User();
        u.setId(user.getId());
        rental.setOwner(u);

        rental.setPicture(pictureUrl);

        rentalRepository.save(rental);
    }

   public void updateRental(long id, String name, int surface, int price, String description) {
       Optional<Rental> rentalOptional = rentalRepository.findById(id);

       if (!rentalOptional.isPresent()) {
           throw new IllegalArgumentException("Rental with ID " + id + " not found");
       }

       Rental rental = rentalOptional.get();

       if (name.isEmpty() || surface <= 0 || price <= 0 || description.isEmpty()) {
           throw new IllegalArgumentException("Name, surface, price, and description are required fields");
       }

       rental.setName(name);
       rental.setSurface(surface);
       rental.setPrice(price);
       rental.setDescription(description);
       rental.setUpdated_at(new Date());

       rentalRepository.save(rental);
   }

}
