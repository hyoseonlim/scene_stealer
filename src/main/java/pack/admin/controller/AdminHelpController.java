package pack.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import pack.admin.model.AdminHelpModel;
import pack.dto.NoticeDto;
import pack.entity.Notice;
import pack.repository.NoticesRepository;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AdminHelpController {

	@Autowired
	NoticesRepository repository;
	@Autowired
	private AdminHelpModel adminHelpModel; // AdminHelpModel 인스턴스 주입
	// 공지 전체 목록

	@GetMapping("/admin/help/notice")
	public ResponseEntity<Page<NoticeDto>> getAllNotices(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "category", required = false) String category) {

		Pageable pageable = PageRequest.of(page, size);
		Page<NoticeDto> noticePage = adminHelpModel.getNotices(category, pageable);

		return ResponseEntity.ok(noticePage);
	}

	// 공지 상세보기
	@GetMapping("/admin/help/notice/{no}")
	public NoticeDto getOneNotie(@PathVariable("no") int no) {
		return Notice.toDto(repository.findByNo(no));
	}

	// 공지 추가
	@PostMapping("/admin/help/notice")
	public Map<String, Object> insert(@RequestBody NoticeDto dto) {
		dto.setDate(LocalDateTime.now());
		repository.save(NoticeDto.toEntity(dto));
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
