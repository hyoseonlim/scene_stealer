package pack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Popup;

public interface PopupRepository extends JpaRepository<Popup, Integer> {
	
	public List<Popup> findByIsShowIsTrue();

}
