package com.openclassrooms.javaspring.repository;

import com.openclassrooms.javaspring.model.Rental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends CrudRepository <Rental, Long>{

}
