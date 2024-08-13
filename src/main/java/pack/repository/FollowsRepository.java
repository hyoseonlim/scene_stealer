package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Follow;

public interface FollowsRepository extends JpaRepository<Follow, Integer>{

}
