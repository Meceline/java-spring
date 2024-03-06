package com.openclassrooms.javaspring.repository;

import com.openclassrooms.javaspring.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{

}

