package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer>{

}
