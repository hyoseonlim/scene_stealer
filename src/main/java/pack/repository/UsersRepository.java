package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{

}
