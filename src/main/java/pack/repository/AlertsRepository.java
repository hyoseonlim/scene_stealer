package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Alert;

public interface AlertsRepository extends JpaRepository<Alert, Integer>{

}
