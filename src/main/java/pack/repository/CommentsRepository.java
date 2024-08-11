package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer>{

}
