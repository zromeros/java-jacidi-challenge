package com.jacidizadkiel.javazadkieljacidi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.jacidizadkiel.javazadkieljacidi.models.Membership;


@Repository
public interface MembershipRepository extends MongoRepository<Membership, String> {
    Membership findByKey(String key);
}