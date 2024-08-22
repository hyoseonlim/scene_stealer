package pack.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Alert;

public interface AlertsRepository extends JpaRepository<Alert, Integer>{

//	public List<Alert> findByUserNo(int userNo);
	
	public int deleteByNo(int alertNo);
	
	public Page<Alert> findByUserNo(int userNo, Pageable pageable);
}
