package com.openclassrooms.javaspring.controller;

import com.openclassrooms.javaspring.dto.*;
import com.openclassrooms.javaspring.model.Rental;
import com.openclassrooms.javaspring.model.User;
import com.openclassrooms.javaspring.service.FileUpload;
import com.openclassrooms.javaspring.service.JWTService;
import com.openclassrooms.javaspring.service.RentalService;
import com.openclassrooms.javaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    public UserService userService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRental(@PathVariable("id") final String id_string){
        try{
            long id = Long.parseLong(id_string);
            Rental rental = rentalService.getRental(id).orElse(null);

            if (rental == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertRentalToRentalResponse(rental));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid rental ID format");
        } catch (Exception e) {
            return handleErrorResponse(e);
        }
    }

    @GetMapping("/rentals")
    public ResponseEntity<?> getRentals() {
        try {
            Iterable<Rental> rentals = rentalService.getRentals();
            List<RentalResponse> list = new ArrayList<>();

            for (Rental rental : rentals) {
                list.add(convertRentalToRentalResponse(rental));
            }

            Map<String, List<RentalResponse>> responseBody = new HashMap<>();
            responseBody.put("rentals", list);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return handleErrorResponse(e);
        }
    }

    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(@RequestParam("name") String name,
                                          @RequestParam("surface") int surface,
                                          @RequestParam("price") int price,
                                          @RequestParam("description") String description,
                                          @RequestParam("picture") MultipartFile picture) {
        try {
            // Gestion de l'authentification
            UserResponse user = getUserFromToken();

            // Appel de la méthode du service pour créer la location
            rentalService.createRental(name, surface, price, description, picture, user);

            return ResponseEntity.ok(Collections.singletonMap("message", "Rental created!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading picture");
        } catch (Exception e) {
            return handleErrorResponse(e);
        }
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable("id") final String id_string,
                                          @RequestParam("name") String name,
                                          @RequestParam("surface") int surface,
                                          @RequestParam("price") int price,
                                          @RequestParam("description") String description) {
        try {
            long id = Long.parseLong(id_string);

            // mise à jour de la location
            rentalService.updateRental(id, name, surface, price, description);

            return ResponseEntity.ok(Collections.singletonMap("message", "Rental updated!"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid rental ID format");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return handleErrorResponse(e);
        }
    }




    private RentalResponse convertRentalToRentalResponse(Rental rental) {
        RentalResponse rentalResponse = new RentalResponse();
        rentalResponse.setId(rental.getId());
        rentalResponse.setName(rental.getName());
        rentalResponse.setSurface(rental.getSurface());
        rentalResponse.setPrice(rental.getPrice());
        rentalResponse.setPicture(rental.getPicture());
        rentalResponse.setDescription(rental.getDescription());

        if (rental.getOwner() != null) {
            rentalResponse.setOwner_id(rental.getOwner().getId());
        }

        rentalResponse.setCreated_at(rental.getCreated_at());
        rentalResponse.setUpdated_at(rental.getUpdated_at());

        return rentalResponse;
    }

    private UserResponse getUserFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                return jwtService.getUser((Jwt) principal);
            }
        }
        return new UserResponse();
    }

    private ResponseEntity<?> handleErrorResponse(Exception e) {
        RentalResponse errorResponse = new RentalResponse();
        errorResponse.setName("An error occurred while processing your rentals request");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}
