/*
 * Copyright (c) mithilesh89ece@gmail.com. All Rights Reserved.
 * ============================================================
 */
package nl.abn.assignment.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.abn.assignment.entities.User;
import nl.abn.assignment.exception.ErrorResponse;
import nl.abn.assignment.service.UserService;

/**
 * To handle the User and role management
 */
@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;



    @Operation(summary = "Register the new user; Role Required: ROLE_MANAGER")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
                    @ApiResponse(responseCode = "201", description = "Return the created User",
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Exception",
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User appUser) {
        return new ResponseEntity<>(userService.saveUser(appUser), HttpStatus.CREATED);
    }

}

