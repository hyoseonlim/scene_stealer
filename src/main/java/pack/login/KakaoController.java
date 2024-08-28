package pack.login;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import pack.entity.User;
import pack.repository.UsersRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {

    @Value("${kakao.api-url.user-info}")
    private String kakaoUserInfoUrl;

    private final UsersRepository usersRepository;

    public KakaoController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostMapping
    public ResponseEntity<User> handleKakaoLogin(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("accessToken");
        
        try {
            URL url = new URL(kakaoUserInfoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                conn.disconnect();

                // JSON 파싱 (예: Jackson ObjectMapper를 사용하여 파싱)
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> userInfo = mapper.readValue(content.toString(), Map.class);

                // 이후 기존 코드대로 처리
                String kakaoId = String.valueOf(userInfo.get("id"));
                Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
//                String email = (String) kakaoAccount.get("email");
//                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//                String nickname = (String) profile.get("nickname");

                // 사용자 정보 저장 또는 업데이트
                Optional<User> userOptional = usersRepository.findById(kakaoId);
                User user;
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                } else {
                    user = new User();
                    user.setId(kakaoId);
//                    user.setEmail(email);
//                    user.setNickname(nickname);
                    usersRepository.save(user);
                }

                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
