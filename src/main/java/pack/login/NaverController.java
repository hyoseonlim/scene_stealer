package pack.login;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pack.entity.User;
import pack.repository.UsersRepository;

@RestController
@RequestMapping("/api/naver")
public class NaverController {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Value("${naver.api-url.token}")
    private String naverTokenUrl;

    @Value("${naver.api-url.user-info}")
    private String naverUserInfoUrl;

    @Autowired
    private UsersRepository urps;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getNaverToken(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        String state = payload.get("state");

        try {
            // 네이버로부터 액세스 토큰 요청
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(naverTokenUrl + "?grant_type=authorization_code&client_id=" + clientId
                        + "&client_secret=" + clientSecret + "&code=" + code + "&state=" + state))
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String content = response.body();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> tokenInfo = mapper.readValue(content, Map.class);
                return ResponseEntity.ok(tokenInfo);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> handleNaverLogin(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("accessToken");

        try {
            // 액세스 토큰으로 사용자 정보 요청
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(naverUserInfoUrl))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String content = response.body();
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> userInfo = mapper.readValue(content, Map.class);

                Map<String, Object> userInfoResponse = (Map<String, Object>) userInfo.get("response");
                String naverId = String.valueOf(userInfoResponse.get("id"));
                String nickname = String.valueOf(userInfoResponse.get("nickname"));
                String name = String.valueOf(userInfoResponse.get("name"));
                String email = String.valueOf(userInfoResponse.get("email"));
                String mobile = String.valueOf(userInfoResponse.get("mobile"));

                Optional<User> userOptional = urps.findById(naverId);
                User user;
                Map<String, Object> result;

                if (userOptional.isPresent()) {
                    user = userOptional.get();
                    result = Map.of("status", "login", "user", user);
                } else {
                    user = User.builder()
                        .id(naverId)
                        .email(email)
                        .nickname(nickname)
                        .name(name)
                        .tel(mobile)
                        .subpath("naver")
                        .build();
                    urps.save(user);
                    result = Map.of("status", "signup", "user", user);
                }

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/delete-token")
    public ResponseEntity<String> deleteNaverToken(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("accessToken");

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=" + clientId + "&client_secret=" + clientSecret + "&access_token=" + accessToken + "&service_provider=NAVER"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok("Token deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to delete token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting token");
        }
    }
}
