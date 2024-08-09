package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.PostComments;

public interface PostCommentsRepository extends JpaRepository<PostComments, Integer>{

}
