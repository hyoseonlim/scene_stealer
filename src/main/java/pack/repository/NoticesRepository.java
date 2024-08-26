package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Notice;

public interface NoticesRepository extends JpaRepository<Notice, Integer>{
	public Notice findByNo(int no);
	
	public Page<Notice> findAll(Pageable pageable);

    // 카테고리별로 공지사항을 페이징 처리하는 메서드
    Page<Notice> findByCategory(String category, Pageable pageable);
}
