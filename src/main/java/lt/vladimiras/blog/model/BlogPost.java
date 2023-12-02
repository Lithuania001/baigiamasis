package lt.vladimiras.blog.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class BlogPost {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Long id;
    private String text;

    @JsonManagedReference
    @ManyToOne
    private User author;
    private LocalDateTime date;

}
