package com.jeanmrtns.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping()
    public ResponseEntity createUser(@RequestBody User user) {
        User userAlreadyExists = this.userRepository.findByEmail(user.getEmail());

        if (userAlreadyExists != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        var passwordHashed = BCrypt.withDefaults().hash(12, user.getPassword().toCharArray()).toString();

        user.setPassword(passwordHashed);

        User createdUser = this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
