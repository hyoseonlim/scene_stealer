package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Integer>{

}
