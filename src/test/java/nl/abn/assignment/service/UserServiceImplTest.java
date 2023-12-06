package nl.abn.assignment.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.abn.assignment.entities.User;
import nl.abn.assignment.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testSaveUser(){
        when(passwordEncoder.encode(anyString())).thenReturn("random");
        when(userRepository.save(any())).thenReturn(userDetails());
        User user = userService.saveUser(userDetails());
        assertEquals("random", user.getUsername());
        assertEquals("random", user.getPassword());
    }

    @Test
    void testGetUser(){
        when(userRepository.findByUsername(anyString())).thenReturn(userDetails());
        User user = userService.getUser("random");
        assertEquals("random", user.getUsername());
        assertEquals("random", user.getPassword());
    }


//    @Test
//    void testAddRoleToUser(){
//
//        when(userRepository.findByName("mithilesh")).thenReturn(userDetails());
//        when(roleRepository.findByName("NEW_ROLE")).thenReturn(newRole());
//        assertDoesNotThrow((
//                        () -> userService.addRoleToUser("mithilesh","NEW_ROLE")
//        ));
//
//    }

    private User userDetails() {
        return User.builder()
            .id("12121212")
            .username("random")
            .password("random")
            .userRoles(Set.of("ROLE_USER", "ROLE_ADMIN"))
            .build();
    }

}
