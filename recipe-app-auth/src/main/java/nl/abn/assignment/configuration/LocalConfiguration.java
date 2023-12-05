package nl.abn.assignment.configuration;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import nl.abn.assignment.entities.User;
import nl.abn.assignment.entities.UserRole;
import nl.abn.assignment.service.UserService;

@Configuration
@Profile("dev")
public class LocalConfiguration {

    /**
     * To update the Default user with ROLE_MANAGER role to make testing easier
     *
     * @param userService
     * @return
     */
    @Bean
    @DependsOn({"userServiceImpl"})
    CommandLineRunner createSampleUserRoleData(UserService userService) {
        return args -> {
            userService.saveUser(new User(null, "mithilesh" , "admin", Set.of(UserRole.ROLE_USER.name(), UserRole.ROLE_ADMIN.name())));

        };
    }
}
