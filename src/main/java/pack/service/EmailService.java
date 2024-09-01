package pack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import pack.dto.MailDto;
import pack.entity.User;
import pack.repository.UsersRepository;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // 발신자 이메일 주소를 application.properties에서 가져오기
    private String fromEmail;

    // 생성자
    public EmailService(UsersRepository usersRepository, JavaMailSender javaMailSender) {
        this.usersRepository = usersRepository;
        this.javaMailSender = javaMailSender;
    }

    /**
     * 사용자 가입 시 환영 이메일 발송
     * @param user 가입한 사용자 정보
     */
    public void sendWelcomeEmail(User user) {
        MailDto mailDto = createWelcomeMailDto(user);
        try {
            sendEmail(mailDto);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send welcome email.", e);
        }
    }

    /**
     * 이메일 인증 번호 생성
     * @return 생성된 인증 번호
     */
    private int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
    }

    /**
     * 인증 이메일 생성
     * @param mail 수신자 이메일 주소
     * @param code 인증 코드
     * @return 생성된 MimeMessage 객체
     * @throws MessagingException 이메일 생성 중 오류 발생 시
     */
    public MimeMessage createVerificationMail(String mail, int code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(mail);
        helper.setSubject("[SceneStealer] 이메일 인증 코드");

        String body = "<h3>SceneStealer</h3>" +
                      "요청하신 인증 코드는 <b>" + code + "</b>입니다.<br/><br/>" +
                      "해당 코드는 10분 뒤 만료됩니다.<br/><br/>" +
                      "감사합니다.";
        helper.setText(body, true); // HTML 포맷으로 메시지 설정

        return message;
    }

    /**
     * 인증 이메일 발송 및 코드 반환
     * @param mail 수신자 이메일 주소
     * @return 생성된 인증 코드
     */
    public int sendMail(String mail) {
        int code = generateVerificationCode();
        try {
            MimeMessage message = createVerificationMail(mail, code);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email.", e);
        }
        return code;
    }

    /**
     * 실제 이메일 발송 로직
     * @param mailDto 이메일 정보를 담고 있는 DTO
     * @throws MessagingException 이메일 발송 중 오류 발생 시
     */
    private void sendEmail(MailDto mailDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        mimeMessageHelper.setTo(mailDto.getReceiver());
        mimeMessageHelper.setSubject(mailDto.getTitle());
        mimeMessageHelper.setText(mailDto.getMessage(), false);

        javaMailSender.send(mimeMessage);
    }

    /**
     * 환영 이메일 DTO 생성
     * @param user 가입한 사용자 정보
     * @return MailDto 객체
     */
    private MailDto createWelcomeMailDto(User user) {
        MailDto mailDto = new MailDto();
        mailDto.setSender(fromEmail);
        mailDto.setReceiver(user.getEmail());
        mailDto.setTitle("[SceneStealer] 회원가입 환영합니다!");
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
        return mailDto;
    }
}