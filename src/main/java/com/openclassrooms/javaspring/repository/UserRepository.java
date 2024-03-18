package com.openclassrooms.javaspring.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import com.openclassrooms.javaspring.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmailAndPassword(String email, String password);

    User findUserByEmailAndNameAndPassword(String email, String name, String password);

    User findByName(String name);
}
