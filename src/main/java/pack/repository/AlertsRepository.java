package pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Alert;

public interface AlertsRepository extends JpaRepository<Alert, Integer>{

//	public List<Alert> findByUserNo(int userNo);
	
	public int deleteByNo(int alertNo);
	
	public List<Alert> findByUserNoAndIsReadFalse(int userNo);
	
	public Page<Alert> findByUserNoOrderByNoDesc(int userNo, Pageable pageable);
}
