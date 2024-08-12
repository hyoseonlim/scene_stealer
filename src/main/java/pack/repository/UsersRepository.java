package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer>{

}
