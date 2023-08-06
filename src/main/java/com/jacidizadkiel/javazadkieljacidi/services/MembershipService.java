package com.jacidizadkiel.javazadkieljacidi.services;

import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;
import com.jacidizadkiel.javazadkieljacidi.repositories.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    public Membership getMembershipById(String id) {
        return membershipRepository.findById(id).orElse(null);
    }

    public Membership createMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    public Membership findByKey(String key) {
        return membershipRepository.findByKey(key);
    }

    public Membership updateMembership(Membership updatedMembership) {
        String id = updatedMembership.getId();
        Membership existingMembership = getMembershipById(id);
        
        if (existingMembership == null) {
            throw new ResourceNotFoundException("Membership not found with id: " + id);
        }

        existingMembership.setKey(updatedMembership.getKey());
        existingMembership.setName(updatedMembership.getName());
        existingMembership.setPrio(updatedMembership.getPrio());
        existingMembership.setDuration(updatedMembership.getDuration());

        return membershipRepository.save(existingMembership);
    }

    public void deleteMembershipById(String id) {
        Optional<Membership> membershipOptional = membershipRepository.findById(id);
        if (membershipOptional.isPresent()) {
            Membership membership = membershipOptional.get();
            membershipRepository.delete(membership);
        } else {
            throw new ResourceNotFoundException("Membership not found with ID: " + id);
        }
    }

}