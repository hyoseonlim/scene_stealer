package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>{

}
