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
    public ResponseEntity<RentalResponse> getRental(@PathVariable("id") final String id_string){
        long id = Long.parseLong(id_string);
        try{
            Rental r = rentalService.getRental(id).orElse(null);

            if (r == null) {
                return null;
            }

            RentalResponse rentalResponse = new RentalResponse();
            rentalResponse.setId(r.getId());
            rentalResponse.setName(r.getName());
            rentalResponse.setSurface(r.getSurface());
            rentalResponse.setPrice(r.getPrice());
            rentalResponse.setPicture(r.getPicture());
            rentalResponse.setDescription(r.getDescription());

            // Vérifier si la location a un propriétaire avant de récupérer son ID
            if (r.getOwner() != null) {
                rentalResponse.setOwner_id(r.getOwner().getId());
            }

            rentalResponse.setCreated_at(r.getCreated_at());
            rentalResponse.setUpdated_at(r.getUpdated_at());

            return ResponseEntity.ok(rentalResponse);
        } catch (Exception e) {
            RentalResponse errorResponse = new RentalResponse();
            errorResponse.setName("An error occurred while processing your request");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalResponse>>> getRentals() {
        try {
            Iterable<Rental> rentals = rentalService.getRentals();
            List<RentalResponse> list = new ArrayList<>();

            for (Rental r : rentals) {
                RentalResponse rentalResponse = new RentalResponse();
                rentalResponse.setId(r.getId());
                rentalResponse.setName(r.getName());
                rentalResponse.setSurface(r.getSurface());
                rentalResponse.setPrice(r.getPrice());
                rentalResponse.setPicture(r.getPicture());
                rentalResponse.setDescription(r.getDescription());

                if (r.getOwner() != null) {
                    rentalResponse.setOwner_id(r.getOwner().getId());
                }
                rentalResponse.setCreated_at(r.getCreated_at());
                rentalResponse.setUpdated_at(r.getUpdated_at());
                list.add(rentalResponse);
            }

            Map<String, List<RentalResponse>> responseBody = new HashMap<>();
            responseBody.put("rentals", list);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            RentalResponse errorResponse = new RentalResponse();
            errorResponse.setName("An error occurred while processing your request");
            Map<String, List<RentalResponse>> responseBody = new HashMap<>();
            responseBody.put("error", Collections.singletonList(errorResponse));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    @PostMapping("/rentals")
    public MessageResponse createRental(@RequestParam("name") String name,
                                        @RequestParam("surface") int surface,
                                        @RequestParam("price") int price,
                                        @RequestParam("description") String description,
                                        @RequestParam("picture") MultipartFile picture) throws IOException {

            System.out.println(name);
        try {
    //Transfert le fichier
            String pictureUrl = fileUpload.uploadFile(picture);
    //Récupère les info du user
            UserResponse user = new UserResponse();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof Jwt) {
                    user = jwtService.getUser((Jwt) principal);
                }
            }

            User u = new User();
            u.setId(user.getId());
        //Prépare le rental
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageResponse response = new MessageResponse();
        response.setMessage("Rental created !");
        return response;
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<Map<String, String>> updateRental(@PathVariable("id") final String id_string,
                                                            @RequestParam("name") String name,
                                                            @RequestParam("surface") int surface,
                                                            @RequestParam("price") int price,
                                                            @RequestParam("description") String description) {
        try{
            long id = Long.parseLong(id_string);
            Optional<Rental> r = rentalService.getRental(id);

            Rental rental = r.get();
            User owner = rental.getOwner();

            User user = userService.getUserById(owner.getId());

            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rental.setUpdated_at(new Date());

            rentalService.updateRental(rental);

            return ResponseEntity.ok(Collections.singletonMap("message", "Rental updated !"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while processing your request"));
        }
    }
}
