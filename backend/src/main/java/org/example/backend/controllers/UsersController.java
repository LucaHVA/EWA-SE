package org.example.backend.controllers;

import org.example.backend.models.User;
import org.example.backend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

/**
 * @author Luca Rijkbost
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UsersController {

private final UsersRepository usersRepository;

@Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping(path = "test", produces = "application/json")
    public List<User> getMockUsers(){
        return List.of(
                new User(10,"Armando","Cringe@ball.nl", "fout"),
                new User(11,"Ballmando","yep@huh.be", "goed")

        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getCabinById(@PathVariable long id) {
        User user = usersRepository.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createCabin(@RequestBody User user) {
        try {
            // Save the cabin to generate a unique ID
            User savedUser = usersRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();

            return ResponseEntity.created(location).body(savedUser);
        } catch (Exception e) {
            // Handle exceptions (e.g., constraint violations) appropriately
            return ResponseEntity.badRequest().build();
        }
    }


}
