package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Notice;

public interface NoticesRepository extends JpaRepository<Notice, Integer>{
	public Notice findByNo(int no);
}
