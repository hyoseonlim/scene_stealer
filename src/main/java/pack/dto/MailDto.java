package pack.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 이메일 발송에 필요한 DTO
public class MailDto {
    private String sender;
    private String receiver;
    private String title;
    private String message;
}
