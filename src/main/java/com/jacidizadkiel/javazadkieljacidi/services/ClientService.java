package com.jacidizadkiel.javazadkieljacidi.services;

import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Client;
import com.jacidizadkiel.javazadkieljacidi.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(String id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public boolean existsByDni(String dni) {
        return clientRepository.existsByDni(dni);
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public void deleteClientById(String id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            clientRepository.delete(client);
        } else {
            throw new ResourceNotFoundException("Client not found with ID: " + id);
        }
    }

    public Client updateClient(Client updatedClient) {
        String id = updatedClient.getId();
        Client existingClient = getClientById(id);
        
        if (existingClient == null) {
            throw new ResourceNotFoundException("Client not found with id: " + id);
        }

        existingClient.setName(updatedClient.getName());
        existingClient.setLastName(updatedClient.getLastName());
        existingClient.setDni(updatedClient.getDni());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setMembership(updatedClient.getMembership());

        return clientRepository.save(existingClient);
    }
}