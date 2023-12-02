package lt.vladimiras.blog.controller;

import lombok.RequiredArgsConstructor;
import lt.vladimiras.blog.dto.BlogPostRequest;
import lt.vladimiras.blog.model.BlogPost;
import lt.vladimiras.blog.model.User;
import lt.vladimiras.blog.repository.BlogPostRepository;
import lt.vladimiras.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/blog-posts")
@RestController
public class BlogPostController {


    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @PostMapping
    public void create(@RequestBody BlogPostRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "not existing user: " + request.getUsername()));

        var post = new BlogPost();
        post.setText(request.getText());
        post.setDate(LocalDateTime.now());
        post.setAuthor(user);

        blogPostRepository.save(post);
    }

    @GetMapping
    public List<BlogPost> getAll() {
        return blogPostRepository.findAll();
    }

    @PutMapping
    public void update() {

    }

    @DeleteMapping
    public void delete() {

    }
}
