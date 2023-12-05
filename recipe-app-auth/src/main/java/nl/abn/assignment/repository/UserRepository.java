package nl.abn.assignment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nl.abn.assignment.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
}