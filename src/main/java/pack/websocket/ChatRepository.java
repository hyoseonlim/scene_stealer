package pack.websocket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
	
	public List<Chat> findByChatuserNo(int chatNo);
	

}
