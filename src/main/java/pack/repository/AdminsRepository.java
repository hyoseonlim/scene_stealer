package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Admin;

public interface AdminsRepository extends JpaRepository<Admin, Integer>{

}
