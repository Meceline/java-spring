package com.openclassrooms.javaspring.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import com.openclassrooms.javaspring.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
