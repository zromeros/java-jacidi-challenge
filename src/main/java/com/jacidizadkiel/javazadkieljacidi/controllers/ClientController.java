package com.jacidizadkiel.javazadkieljacidi.controllers;

import com.jacidizadkiel.javazadkieljacidi.models.Client;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;
import com.jacidizadkiel.javazadkieljacidi.services.ClientService;
import com.jacidizadkiel.javazadkieljacidi.services.MembershipService;
import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.exceptions.BadRequestException;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiOkResponse;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<ApiOkResponse<List<Client>>> getAllClients() {
        List<Client> clients = clientService.getAllClients();

        ApiOkResponse<List<Client>> response = new ApiOkResponse<>("Clients list sent successfully", HttpStatus.OK.value(), clients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable String id) {
        try {

            Client client = clientService.getClientById(id);
            if (client == null) {
                throw new ResourceNotFoundException("Client not found");
            }
            ApiOkResponse<Client> response = new ApiOkResponse<>("Client retrieved successfully", HttpStatus.OK.value(), client);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Client not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));
        }
    
    }

    @PostMapping
    public ResponseEntity<Object> createClient(@RequestBody Client request) {
        try {
    
            if (clientService.existsByDni(request.getDni()) || clientService.existsByEmail(request.getEmail())) {
                throw new BadRequestException("DNI or email already exists");
            }

            Membership freeMembership = membershipService.findByKey("free");

            if (freeMembership == null) {
                throw new BadRequestException("Default membership 'free' not found");
            }

            String freeMembershipId = freeMembership.getId();

            Client client = new Client();
            client.setName(request.getName());
            client.setLastName(request.getLastName());
            client.setDni(request.getDni());
            client.setEmail(request.getEmail());

            client.setLastDelivery(null); 

            Date currentDate = new Date();
            client.setNextRenewal(currentDate);

            client.setMembership(freeMembershipId);

            Client createdClient = clientService.createClient(client);
            ApiOkResponse<Client> response = new ApiOkResponse<>("Client created successfully", HttpStatus.CREATED.value(), createdClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable String id, @RequestBody Client request) {
        try {
            Client existingClient = clientService.getClientById(id);
            
            if (existingClient == null) {
                throw new ResourceNotFoundException("Client not found");
            }

            String newMembershipId = request.getMembership();
            String newName = request.getName();
            String newLastName = request.getLastName();
            String newDni = request.getDni();
            String newEmail = request.getEmail();

            Membership membership = null;
            if (newMembershipId != null) {
                membership = membershipService.getMembershipById(newMembershipId);
                if (membership != null) {
                    existingClient.setMembership(newMembershipId);
                }
            }
            if (newName != null) {
                existingClient.setName(newName);
            }
            if (newLastName != null) {
                existingClient.setLastName(newLastName);
            }
            if (newDni != null) {
                existingClient.setDni(newDni);
            }
            if (newEmail != null) {
                existingClient.setEmail(newEmail);
            }

            Client updatedClient = clientService.updateClient(existingClient);
            ApiOkResponse<Client> response = new ApiOkResponse<>("Client updated successfully", HttpStatus.OK.value(), updatedClient);
            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));

        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Client not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClientById(@PathVariable String id) {
        try {
            clientService.deleteClientById(id);
            ApiOkResponse<Object> response = new ApiOkResponse<>("Client deleted successfully", HttpStatus.OK.value(), null);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Client not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }


}

