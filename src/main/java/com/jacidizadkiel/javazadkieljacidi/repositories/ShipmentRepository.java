package com.jacidizadkiel.javazadkieljacidi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jacidizadkiel.javazadkieljacidi.models.Shipment;

@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {
}