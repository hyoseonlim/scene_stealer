package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Comments;

public interface PostCommentsRepository extends JpaRepository<Comments, Integer>{

}
