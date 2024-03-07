package controllers;
import models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** @author Luca Rijkbost
 *
 */
@RestController
@RequestMapping("/users")
public class UsersController {

//    @GetMapping(path="/test", produces="application/json")
//    public List<User> getTestUsers(){
//        return List.of(
//                new User("Armando", "Cringe@gmail.com","loser")
//        );
//    }
}
