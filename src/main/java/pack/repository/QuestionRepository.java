package pack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pack.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

}
