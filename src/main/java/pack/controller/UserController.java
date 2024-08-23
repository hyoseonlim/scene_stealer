package pack.controller;


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
	public ResponseEntity<Page<NoticeDto>> getNoticeList(Pageable pageable) {
		Page<NoticeDto> noticePage = um.getNoticeList(pageable);
		return ResponseEntity.ok(noticePage);
	}
	
	@GetMapping("/user/notice/{noticeNo}")
	public NoticeDto getNoticeInfo(@PathVariable("noticeNo") int noticeNo) {
		return um.getNoticeInfo(noticeNo);
	}

}
