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
import pack.entity.User;
import pack.repository.UsersRepository;

import java.util.Random;

@Service
public class PasswordResetService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public PasswordResetService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public void resetPassword(FindPassDto findPassDto) {
        User user = usersRepository.findByIdAndEmail(findPassDto.getId(), findPassDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디와 이메일이 일치하는 사용자를 찾을 수 없습니다."));

        String tempPwd = generateTempPassword();
        user.updatePassword(user.getEmail(), bCryptPasswordEncoder.encode(tempPwd));

        sendEmail(findPassDto.getEmail(), tempPwd, user.getId());
    }

    private String generateTempPassword() {
        String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ다라클!@#$%^&*()";
        StringBuilder tempPwd = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(charSet.length());
            tempPwd.append(charSet.charAt(idx));
        }
        return tempPwd.toString();
    }

    private void sendEmail(String recipientEmail, String tempPwd, String userId) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("[SceneStealer] 임시 비밀번호");
            mimeMessageHelper.setText(
                "안녕하세요 " + userId + "님,\n\n" +
                "요청하신 비밀번호 재설정이 완료되었습니다. 아래는 임시 비밀번호입니다:\n\n" +
                "임시 비밀번호: " + tempPwd + "\n\n" +
                "로그인 후에 새 비밀번호로 변경해 주세요.\n\n" +
                "만약 이 이메일을 요청하지 않으셨거나 계정에 문제가 있는 경우, 고객 지원팀에 연락해 주시기 바랍니다.\n\n" +
                "감사합니다!\n\n" +
                "SceneStealer 팀 드림", false);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email.", e);
        }
    }
}