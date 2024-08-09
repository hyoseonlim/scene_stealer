package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Follows;

public interface FollowsRepository extends JpaRepository<Follows, Integer>{

}
