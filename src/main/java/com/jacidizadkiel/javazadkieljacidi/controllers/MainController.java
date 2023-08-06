package com.jacidizadkiel.javazadkieljacidi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jacidizadkiel.javazadkieljacidi.services.MainService;
import com.jacidizadkiel.javazadkieljacidi.services.ShipmentService;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiErrorResponse;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiOkResponse;
import com.jacidizadkiel.javazadkieljacidi.exceptions.BadRequestException;
import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Client;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;
import com.jacidizadkiel.javazadkieljacidi.models.Shipment;
import com.jacidizadkiel.javazadkieljacidi.services.ClientService;
import com.jacidizadkiel.javazadkieljacidi.services.MembershipService;

@RestController
@RequestMapping()
public class MainController {

    @Autowired
    private MainService mainService;
 
    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MembershipService membershipService;

    @PostMapping("/ship/removeProduct")
    public ResponseEntity<Object> removeProductFromShipment(@RequestParam String productId, @RequestParam String shipmentId) {
        try {
            Shipment shipment = shipmentService.getShipmentById(shipmentId);
            if (shipment == null) {
                throw new ResourceNotFoundException("Shipment not found");
            }

            Client client = clientService.getClientById(shipment.getClient());
            if (client == null) {
                throw new BadRequestException("Client not found");
            }

            Membership membership = membershipService.getMembershipById(client.getMembership());
            if (membership == null) {
                throw new BadRequestException("Membership not found");
            }

            mainService.removeProductFromShipment(productId, shipment, membership.getPrio());
            Shipment updatedShipment = shipmentService.createShipment(shipment, membership.getPrio());
            ApiOkResponse<Shipment> response = new ApiOkResponse<>("Product removed successfully", HttpStatus.OK.value(), updatedShipment);
            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "INTERNAL_SERVER_ERROR", null)));
        }
    }

    @PostMapping("/client/renewMembership")
    public ResponseEntity<Object> renewClientMembership(@RequestParam String clientId, @RequestParam String membershipId) {
        try {
            mainService.renewMembership(clientId, membershipId);

            ApiOkResponse<Object> response = new ApiOkResponse<>("Client membership renewed successfully", HttpStatus.OK.value(), null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "INTERNAL_SERVER_ERROR", null)));
        }
    }
}