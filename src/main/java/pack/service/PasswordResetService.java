package pack.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import pack.dto.FindPassDto;
import pack.dto.MailDto;
import pack.entity.User;
import pack.repository.UsersRepository;

import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final JavaMailSender javaMailSender;

    public PasswordResetService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public void resetPassword(FindPassDto findPassDto) {
        // 아이디와 이메일로 사용자 확인
        User user = usersRepository.findByIdAndEmail(findPassDto.getId(), findPassDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디와 이메일이 일치하는 사용자를 찾을 수 없습니다."));

        // 임시 비밀번호 생성
        String tempPwd = getTempPassword();
        // 임시 비밀번호 인코딩
        String encodedTempPwd = passwordEncoder(tempPwd);
        // User 정보 업데이트
        user.updatePassword(user.getEmail(), encodedTempPwd);

        // 이메일 송부 (트랜잭션 외부에서 수행)
        sendEmail(findPassDto, tempPwd, user);
    }

    // 임시 비밀번호 생성
    public String getTempPassword() {
        String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ다라클!@#$%^&*()";
        StringBuilder tempPwd = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(charSet.length());
            tempPwd.append(charSet.charAt(idx));
        }
        return tempPwd.toString();
    }

    // 임시 비밀번호 인코딩
    public String passwordEncoder(String tempPwd) {
        return bCryptPasswordEncoder.encode(tempPwd);
    }

    // 이메일 발송 메서드
    private void sendEmail(FindPassDto findPassDto, String tempPwd, User user) {
        MailDto mailDto = new MailDto();
        mailDto.setSender("choeglee@gmail.com"); // 발신자 이메일 주소
        mailDto.setReceiver(findPassDto.getEmail()); // 수신자 이메일 주소
        mailDto.setTitle("[SceneStealer] 임시 비밀번호"); // 이메일 제목
        mailDto.setMessage(
                "안녕하세요 " + user.getId() + "님,\n\n" +
                "요청하신 비밀번호 재설정이 완료되었습니다. 아래는 임시 비밀번호입니다:\n\n" +
                "임시 비밀번호: " + tempPwd + "\n\n" +
                "로그인 후에 새 비밀번호로 변경해 주세요.\n\n" +
                "만약 이 이메일을 요청하지 않으셨거나 계정에 문제가 있는 경우, 고객 지원팀에 연락해 주시기 바랍니다.\n\n" +
                "감사합니다!\n\n" +
                "SceneStealer 팀 드림"
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