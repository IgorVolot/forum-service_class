package ait.cohort34.post.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class Post {
    String id;
    String title;
    String content;
    String author;
    LocalDateTime dateCreated;
    Set<String> tags;
    int likes;
    List<Comment> comments;
}
