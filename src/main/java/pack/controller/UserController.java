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

	

}
