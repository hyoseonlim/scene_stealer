package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.PostLike;

public interface PostLikesRepository extends JpaRepository<PostLike, Integer>{

}
