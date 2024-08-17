package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.User;

public interface UsersRepository extends JpaRepository<User, Integer>{
}
