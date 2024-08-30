package pack.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import pack.dto.MailDto;
import pack.entity.User;
import pack.repository.UsersRepository;

@Service
public class EmailService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailService(UsersRepository usersRepository, JavaMailSender javaMailSender) {
        this.usersRepository = usersRepository;
        this.javaMailSender = javaMailSender;
    }

    // 사용자 가입 시 환영 이메일 발송
    public void sendWelcomeEmail(User user) {
        MailDto mailDto = new MailDto();
        mailDto.setSender("choeglee@gmail.com"); // 발신자 이메일 주소
        mailDto.setReceiver(user.getEmail()); // 수신자 이메일 주소
        mailDto.setTitle("[SceneStealer] 회원가입 환영합니다!"); // 이메일 제목
        mailDto.setMessage(
                "안녕하세요 " + user.getId() + "님,\n\n" +
                "SceneStealer에 회원가입이 성공적으로 완료되었습니다! 🎉\n\n" +
                "이제 다양한 서비스와 기능을 이용하실 수 있습니다. 사용 중 궁금한 점이나 도움이 필요하시면 언제든지 고객 지원팀에 문의해 주세요.\n\n" +
                "우리는 항상 여러분의 소중한 의견을 기다리고 있습니다. 즐거운 시간 되세요!\n\n" +
                "감사합니다!\n\n" +
                "SceneStealer 팀 드림\n\n" +
                "[SceneStealer 웹사이트 링크]\n" +
                "[고객 지원 이메일 또는 전화번호]"
        );

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(mailDto.getReceiver()); // 수신자 설정
            mimeMessageHelper.setSubject(mailDto.getTitle()); // 제목 설정
            mimeMessageHelper.setText(mailDto.getMessage(), false); // 메시지 설정

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email.", e);
        }
    }
}