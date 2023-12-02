package lt.vladimiras.blog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Long id;

    private String username;

    @JsonBackReference
    @OneToMany(mappedBy = "author")
    private List<BlogPost> blogPosts;
}
