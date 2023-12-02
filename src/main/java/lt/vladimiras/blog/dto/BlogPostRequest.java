package lt.vladimiras.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPostRequest {

    private String text;
    private String username;

}
