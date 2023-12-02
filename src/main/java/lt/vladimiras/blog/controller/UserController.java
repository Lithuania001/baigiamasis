package lt.vladimiras.blog.controller;

import lombok.RequiredArgsConstructor;
import lt.vladimiras.blog.dto.UserRequest;
import lt.vladimiras.blog.model.User;
import lt.vladimiras.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {


    private final UserRepository repository;

    @PostMapping
    public void create(@RequestBody UserRequest request) {
        repository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already exists: " + user.getUsername());
        });

        var user = new User();
        user.setUsername(request.getUsername());
        repository.save(user);
    }

    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    @PutMapping
    public void update() {

    }

    @DeleteMapping
    public void delete() {

    }
}
