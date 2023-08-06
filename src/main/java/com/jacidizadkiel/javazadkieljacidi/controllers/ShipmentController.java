package com.jacidizadkiel.javazadkieljacidi.controllers;

import com.jacidizadkiel.javazadkieljacidi.models.Shipment;
import com.jacidizadkiel.javazadkieljacidi.models.Client;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;
import com.jacidizadkiel.javazadkieljacidi.services.ShipmentService;
import com.jacidizadkiel.javazadkieljacidi.services.ClientService;
import com.jacidizadkiel.javazadkieljacidi.services.MembershipService;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.jacidizadkiel.javazadkieljacidi.dtos.ApiErrorResponse;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiOkResponse;
import com.jacidizadkiel.javazadkieljacidi.exceptions.BadRequestException;
import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;


@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<Object> getAllShipments() {
        List<Shipment> shipments = shipmentService.getAllShipments();
        ApiOkResponse<List<Shipment>> response = new ApiOkResponse<>("Shipment list sent successfully", HttpStatus.OK.value(), shipments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getShipmentById(@PathVariable String id) {
        try {

            Shipment shipment = shipmentService.getShipmentById(id);
            if (shipment == null) {
                throw new ResourceNotFoundException("Shipment not found");
            }
            ApiOkResponse<Shipment> response = new ApiOkResponse<>("Shipment retrieved successfully", HttpStatus.OK.value(), shipment);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Shipment not found", HttpStatus.NOT_FOUND.value(),
                    new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createShipment(@RequestBody Shipment shipment) {
        try {

            String clientId = shipment.getClient();
            if (clientId == null) {
                throw new BadRequestException("Client field can't be null");
            }

            Client client = clientService.getClientById(clientId);
            if (client == null) {
                throw new BadRequestException("Client not found");
            }

            Date currentDate = new Date();
            if (client.getNextRenewal().before(currentDate)) {
                throw new BadRequestException("Client membership expired");
            }

            Membership membership = membershipService.getMembershipById(client.getMembership());

            if (membership == null) {
                throw new BadRequestException("Membership not found");
            }

            Shipment createdShipment = shipmentService.createShipment(shipment, membership.getPrio());
            ApiOkResponse<Shipment> response = new ApiOkResponse<>("Shipment created successfully", HttpStatus.CREATED.value(), createdShipment);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));


        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "INTERNAL_SERVER_ERROR", null)));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShipmentById(@PathVariable String id) {
        try {

            shipmentService.deleteShipmentById(id);
            ApiOkResponse<Object> response = new ApiOkResponse<>("Shipment deleted successfully", HttpStatus.OK.value(), null);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Shipment not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }

}