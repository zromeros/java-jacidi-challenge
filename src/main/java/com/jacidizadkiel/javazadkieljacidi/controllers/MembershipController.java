package com.jacidizadkiel.javazadkieljacidi.controllers;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;
import com.jacidizadkiel.javazadkieljacidi.services.MembershipService;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiErrorResponse;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiOkResponse;
import com.jacidizadkiel.javazadkieljacidi.exceptions.BadRequestException;
import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<Object> getAllMemberships() {
        List<Membership> memberships = membershipService.getAllMemberships();
        ApiOkResponse<List<Membership>> response = new ApiOkResponse<>("Membership list sent successfully", HttpStatus.OK.value(), memberships);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMembershipById(@PathVariable String id) {
        try {
            Membership membership = membershipService.getMembershipById(id);
            ApiOkResponse<Membership> response = new ApiOkResponse<>("Membership retrieved successfully", HttpStatus.OK.value(), membership);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Membership not found", HttpStatus.NOT_FOUND.value(),
                    new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createMembership(@RequestBody Membership membership) {

        try {

            Membership createdMembership = membershipService.createMembership(membership);
            ApiOkResponse<Membership> response = new ApiOkResponse<>("Membership created successfully", HttpStatus.CREATED.value(), createdMembership);

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
    public ResponseEntity<Object> updateMembership(@PathVariable String id, @RequestBody Membership request) {
        try {
            Membership existingMembership = membershipService.getMembershipById(id);
            
            if (existingMembership == null) {
                throw new ResourceNotFoundException("Membership not found");
            }

            String newKey = request.getKey();
            String newName = request.getName();
            Integer newPrio = request.getPrio();
            Long newDuration = request.getDuration();

            if (newKey != null) {
                existingMembership.setKey(newKey);
            }

            if (newName != null) {
                existingMembership.setName(newName);
            }
       
            if (newPrio != null) {
                existingMembership.setPrio(newPrio);
            }
            if (newDuration != null) {
                existingMembership.setDuration(newDuration);
            }

            Membership updatedMembership = membershipService.updateMembership(existingMembership);
            ApiOkResponse<Membership> response = new ApiOkResponse<>("Membership updated successfully", HttpStatus.OK.value(), updatedMembership);

            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Membership not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMembershipById(@PathVariable String id) {
        try {
            membershipService.deleteMembershipById(id);
            ApiOkResponse<Object> response = new ApiOkResponse<>("Membership deleted successfully", HttpStatus.OK.value(), null);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Membership not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }

}