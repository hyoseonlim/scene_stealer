package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Post;

public interface PostsRepository extends JpaRepository<Post, Integer>{

}
