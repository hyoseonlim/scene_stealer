package pack.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pack.dto.NoticeDto;
import pack.model.UserModel;

@RestController
public class UserController {

	@Autowired
	private UserModel um;

	@GetMapping("/user/notice")
	public ResponseEntity<Page<NoticeDto>> getNoticeList(Pageable pageable) {
		Page<NoticeDto> noticePage = um.getNoticeList(pageable);
		return ResponseEntity.ok(noticePage);
	}

	@GetMapping("/user/notice/{noticeNo}")
	public NoticeDto getNoticeInfo(@PathVariable("noticeNo") int noticeNo) {
		return um.getNoticeInfo(noticeNo);
	}

	@PostMapping("/api/kakao")
	public ResponseEntity<Map<String, Object>> handleKakaoToken(@RequestBody Map<String, String> request) {
		 String accessToken = request.get("accessToken");
		    String userInfo = getUserInfoFromKakao(accessToken);

		    // 사용자 정보를 JSON으로 변환
		    ObjectMapper objectMapper = new ObjectMapper();
		    Map<String, Object> userMap = new HashMap<>();
		    try {
		        userMap = objectMapper.readValue(userInfo, new TypeReference<Map<String, Object>>(){});
		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		    }

		    return ResponseEntity.ok(userMap);
	}

	private String getUserInfoFromKakao(String accessToken) {
		String userInfo = "";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			HttpEntity<String> entity = new HttpEntity<>("", headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET,
					entity, String.class);

			userInfo = response.getBody();
		} catch (HttpClientErrorException e) {
			// 오류 처리
			e.printStackTrace();
		}
		return userInfo;
	}

}
