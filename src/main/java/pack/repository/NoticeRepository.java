package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Notices;

public interface NoticeRepository extends JpaRepository<Notices, Integer>{

}
