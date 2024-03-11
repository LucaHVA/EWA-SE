package org.example.backend.controllers;

import org.example.backend.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Luca Rijkbost
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @GetMapping(path = "test", produces = "application/json")
    public List<User> getMockUsers(){
        return List.of(
                new User("Armando","Cringe@ball.nl", "fout"),
                new User("Ballmando","yep@huh.be", "goed")

        );
    }
}
