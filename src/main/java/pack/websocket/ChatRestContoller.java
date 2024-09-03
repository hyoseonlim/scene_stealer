package pack.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RestController
public class ChatRestContoller {

	@Autowired
	private ChatRepository crps;

	@Autowired
	private ChatUserRepository curps;

	@GetMapping("/chat/admin")
	public List<ChatUserDto> getChatList() {
		return curps.findAll().stream().map(ChatUser::toDto).collect(Collectors.toList());
	}
	
	@GetMapping("/chat/admin/{chatNo}")
	public List<ChatDto> getChatData (@PathVariable("chatNo") int chatNo) {
		return crps.findByChatuserNo(chatNo).stream().map(Chat::toDto).collect(Collectors.toList());
	}

	@GetMapping("/chat/user/{userNo}")
	public Map<String, Object> getChat(@PathVariable("userNo") int userNo) {
		Map<String, Object> result = new HashMap<String, Object>();

		Optional<ChatUser> chatuser = curps.findByUserNoAndCloseChatIsFalse(userNo);

		if (chatuser.isPresent()) {
			ChatUserDto cud = ChatUser.toDto(chatuser.get());
			result.put("chatNo", cud);
			List<Chat> chats = crps.findByChatuserNo(cud.getNo());
			result.put("chats", chats.stream().map(Chat::toDto).collect(Collectors.toList()));
		} else {
			ChatUserDto cud = ChatUserDto.builder().userNo(userNo).closeChat(false).build();
			ChatUser cu = curps.save(ChatUserDto.toEntity(cud));
			result.put("create", cu);
		}

		return result;
	}
	
	@PutMapping("/chat/user/{userNo}/{chatNo}")
	public Map<String, Boolean> closeChat(@PathVariable("userNo") int userNo, @PathVariable("chatNo") int chatNo) {
		 Map<String, Boolean> result = new HashMap<String, Boolean>();
		 result.put("result", closeChatProc(userNo, chatNo));
		 return result;
	}
	
	@PutMapping("/chat/user/{userNo}/{chatNo}/{category}")
	public Map<String, Boolean> selectCategory(@PathVariable("userNo") int userNo, @PathVariable("chatNo") int chatNo, @PathVariable("category") String category) {
		 Map<String, Boolean> result = new HashMap<String, Boolean>();
		 result.put("result", selectCategoryProc(userNo, chatNo, category));
		 return result;
	}
	
	@Transactional
	public boolean closeChatProc(int userNo, int chatNo) {
		try {
			Optional<ChatUser>  cu = curps.findById(chatNo);
			if(cu.isPresent()) {
				ChatUser c = cu.get();
				c.setCloseChat(true);
				curps.save(c);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("closeChatProc ERROR : " + e.getMessage());
			return false;
		}
	}
	

	@Transactional
	public boolean selectCategoryProc(int userNo, int chatNo, String category) {
		try {
			Optional<ChatUser>  cu = curps.findById(chatNo);
			if(cu.isPresent()) {
				ChatUser c = cu.get();
				c.setCategory(category);
				curps.save(c);
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("selectCategoryProc ERROR : " + e.getMessage());
			return false;
		}
	}

}
