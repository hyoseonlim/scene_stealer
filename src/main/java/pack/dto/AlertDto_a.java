package pack.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pack.entity.Alert;
import pack.entity.User;
import pack.repository.UsersRepository;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto_a {
    private Integer no;
    private String category; 
    private String content;
    private java.util.Date date;
    private Integer userNo;
    private Boolean isRead;
    private String path;
    private int totalPages, currentPage;
    private Long totalElements;
    
    public static Alert toEntity(AlertDto_a dto, UsersRepository userRepo) {
        User user = userRepo.findById(dto.getUserNo())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + dto.getUserNo()));
        
        return Alert.builder()
                .no(dto.getNo())
                .user(user)  // Optional에서 추출한 User 객체
                .category(dto.getCategory())
                .content(dto.getContent())
                .date(dto.getDate())
                .isRead(dto.getIsRead())
                .path(dto.getPath())
                .build();
    }
}
