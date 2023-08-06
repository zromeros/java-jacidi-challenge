package com.jacidizadkiel.javazadkieljacidi.services;

import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Shipment;
import com.jacidizadkiel.javazadkieljacidi.repositories.ShipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;


    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(String id) {
        return shipmentRepository.findById(id).orElse(null);
    }

    public Shipment createShipment(Shipment shipment, int membershipPriority) {

      
        Shipment newShipment = new Shipment();
        newShipment.setClient(shipment.getClient());
        newShipment.setProductList(shipment.getProductList(), membershipPriority);
        newShipment.setDeliverDate(shipment.calculateDeliverDate(membershipPriority, shipment.getProductList()));


        return shipmentRepository.save(newShipment);
    }

    public Shipment updateShipment(Shipment updatedShipment, int membershipPriority) {
        String id = updatedShipment.getId();
        Shipment existingShipment = getShipmentById(id);
        
        if (existingShipment == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        existingShipment.setClient(updatedShipment.getClient());
        existingShipment.setProductList(updatedShipment.getProductList(), membershipPriority);

        return shipmentRepository.save(existingShipment);
    }


    public void deleteShipmentById(String id) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findById(id);
        if (shipmentOptional.isPresent()) {
            Shipment shipment = shipmentOptional.get();
            shipmentRepository.delete(shipment);
        } else {
            throw new ResourceNotFoundException("Shipment not found with ID: " + id);
        }
    }

}