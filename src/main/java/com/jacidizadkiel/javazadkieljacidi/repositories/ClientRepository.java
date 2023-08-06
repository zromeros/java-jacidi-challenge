package com.jacidizadkiel.javazadkieljacidi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jacidizadkiel.javazadkieljacidi.models.Client;


@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

}