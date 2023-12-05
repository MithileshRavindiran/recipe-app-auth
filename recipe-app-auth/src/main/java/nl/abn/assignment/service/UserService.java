package nl.abn.assignment.service;

import java.util.List;

import nl.abn.assignment.entities.User;

public interface UserService {

    User saveUser(User appUser);

    User getUser(String userName);

}
