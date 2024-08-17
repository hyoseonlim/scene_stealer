// 공지사항(FAQ)
package pack.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import pack.dto.NoticeDto;
import pack.entity.Notice;
import pack.repository.NoticesRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AdminHelpController {
	
	@Autowired
	NoticesRepository repository;

	// 공지 전체 목록
	@GetMapping("/admin/help/notice")
	public List<NoticeDto> getAllNotice() {
		return repository.findAll().stream().map(Notice::toDto).toList();
	}
	
	// 공지 상세보기
	@GetMapping("/admin/help/notice/{no}")
	public NoticeDto getOneNotie(@PathVariable("no") int no) {
		return Notice.toDto(repository.findByNo(no));
	}
	
	// 공지 추가
	@PostMapping("/admin/help/notice")
	public Map<String, Object> insert(@RequestBody NoticeDto dto) { // @RequestBody: 요청 본문에 담긴 값을 자바객체로 변환
		repository.save(NoticeDto.toEntity(dto));
		/*
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSucceess", true);
		return map;
		이 세 줄을 아래 한 줄로 작성 가능
		*/
		return Map.of("isSuccess", true);
	}
	
	// 공지 수정
	@PutMapping("/admin/help/notice/{no}")
	public Map<String, Object> update(@PathVariable("no") int num, @RequestBody NoticeDto dto) {
		repository.save(NoticeDto.toEntity(dto));
		return Map.of("isSuccess", true);
	}
	
	// 공지 삭제
	@DeleteMapping("/admin/help/notice/{no}")
	public Map<String, Object> delete(@PathVariable("no") int no) {
		repository.deleteById(no);
		return Map.of("isSuccess", true);
	}
	
}
