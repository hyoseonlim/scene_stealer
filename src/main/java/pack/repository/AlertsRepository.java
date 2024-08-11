package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Alerts;

public interface AlertsRepository extends JpaRepository<Alerts, Integer>{

}
