package ait.cohort34.post.dao;

import ait.cohort34.post.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {
    Stream<Post> findAllByAuthorIgnoreCase(String author);

    Stream<Post> findAllByTagsInIgnoreCase(Set<String> tags);

    Stream<Post> findAllByDateCreatedBetween(LocalDate dateFrom, LocalDate dateTo);
}