package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer>{

}
