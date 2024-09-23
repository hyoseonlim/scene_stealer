package pack.websocket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Integer> {

	public List<ChatUser> findByOrderByNoDesc();
	public Optional<ChatUser> findByUserNoAndCloseChatIsFalse(int userNo);
}
