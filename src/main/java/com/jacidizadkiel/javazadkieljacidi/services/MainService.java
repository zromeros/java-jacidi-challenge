package com.jacidizadkiel.javazadkieljacidi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Shipment;
import com.jacidizadkiel.javazadkieljacidi.models.Client;
import com.jacidizadkiel.javazadkieljacidi.repositories.ShipmentRepository;
import com.jacidizadkiel.javazadkieljacidi.repositories.ClientRepository;
import com.jacidizadkiel.javazadkieljacidi.repositories.MembershipRepository;
import com.jacidizadkiel.javazadkieljacidi.models.Product;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;

@Service
public class MainService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MembershipRepository membershipRepository;


    public void removeProductFromShipment(String productId, Shipment shipment, int membershipPriority) {
                
        List<Product> productList = shipment.getProductList();
        
        if (productList == null || productList.isEmpty()) {
            throw new ResourceNotFoundException("Product list is empty in the shipment");
        }
        
        boolean removed = productList.removeIf(p -> p.getId().equals(productId));
        
        if (!removed) {
            throw new ResourceNotFoundException("Product with id " + productId + " is not associated with the shipment");
        }
        
        shipment.setProductList(productList, membershipPriority);
        shipmentRepository.save(shipment); 
    }

    public void renewMembership(String clientId, String membershipId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));

        
        client.setMembership(membership.getId());
        
        long durationInDays = membership.getDuration() / (24 * 60 * 60);
        LocalDate nextRenewalLocalDate = LocalDate.now().plusDays(durationInDays);
        Date nextRenewalDate = java.sql.Date.valueOf(nextRenewalLocalDate);

        client.setNextRenewal(nextRenewalDate);
        clientRepository.save(client); 
    }
}
