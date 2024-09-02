package pack.websocket;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.User;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_chat")
public class ChatUser {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;

    @OneToMany(mappedBy = "chatuser")  // Chat 엔티티와의 관계 설정
    private List<Chat> chats;

    private String category;
    
    private Boolean closeChat;

    public static ChatUserDto toDto(ChatUser entity) {
        return ChatUserDto.builder()
                .no(entity.getNo())
                .userNo(entity.getUser().getNo())
                .userName(entity.getUser().getName())
                .chatsList(entity.getChats().stream().map(Chat::getNo).collect(Collectors.toList()))
                .category(entity.getCategory())
                .closeChat(entity.getCloseChat())
                .build();
    }
}
