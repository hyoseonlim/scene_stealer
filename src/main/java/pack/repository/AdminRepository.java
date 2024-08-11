package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Admins;

public interface AdminRepository extends JpaRepository<Admins, Integer>{

}
