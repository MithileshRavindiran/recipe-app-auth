package nl.abn.assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.entities.User;
import nl.abn.assignment.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User saveUser(User appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }


    @Override
    public User getUser(String userName) {
        return userRepository.findByUsername(userName);
    }

}
