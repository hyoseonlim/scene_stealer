package pack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pack.dto.NoticeDto;
import pack.model.UserModel;

@RestController
public class UserController {
	
	@Autowired
	private UserModel um;
	
	@GetMapping("/user/notice")
	public ResponseEntity<Map<String, Object>> getNoticeList(Pageable pageable) {
		
		Page<NoticeDto> noticePage = um.getNoticeList(pageable);
		Map<String, Object> response = new HashMap<String, Object>();
        response.put("content", noticePage.getContent());
        response.put("totalPages", noticePage.getTotalPages());
        response.put("currentPage", noticePage.getNumber());
        response.put("totalElements", noticePage.getTotalElements());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user/notice/{noticeNo}")
	public NoticeDto getNoticeInfo(@PathVariable("noticeNo") int noticeNo) {
		return um.getNoticeInfo(noticeNo);
	}

}
