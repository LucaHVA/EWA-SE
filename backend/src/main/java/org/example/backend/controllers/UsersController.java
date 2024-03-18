package org.example.backend.controllers;

import jakarta.transaction.Transactional;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.User;
import org.example.backend.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

/**
 * @author Luca Rijkbost & Armando Labega
 */

@RestController
@Transactional
@RequestMapping("/users")
public class UsersController {

    @Qualifier("USERS.JPA")
    @Autowired
    private EntityRepository<User> usersRepository;

    @GetMapping("/test")
    public List<User> getMockUsers(){
        return List.of(
                new User(10L,"Armando","Cringe@ball.nl", "fout"),
                new User(11L,"Ballmando","yep@huh.be", "goed")
        );
    }

    @GetMapping("/all")
    public List<User> getAllScooters() {
        // Call the findAll method from the repository and return the list of all scooters
        return usersRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = usersRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        usersRepository.save(user);

        // Build URI for new scooter and return it with a 201 Created status code
        URI componentBuilder = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(componentBuilder).body(user);
    }
}
