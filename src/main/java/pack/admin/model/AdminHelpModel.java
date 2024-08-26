package pack.admin.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import pack.dto.NoticeDto;
import pack.entity.Notice;
import pack.repository.NoticesRepository;

@Repository
public class AdminHelpModel {
	 @Autowired
	    private NoticesRepository noticesRepository;

	    // 전체 공지사항 또는 카테고리별 공지사항을 가져오는 메서드
	    public Page<NoticeDto> getNotices(String category, Pageable pageable) {
	        Page<Notice> noticePage;

	        if (category == null || category.isEmpty()) {
	            // 전체 공지사항 가져오기
	            noticePage = noticesRepository.findAll(pageable);
	        } else {
	            // 카테고리별 공지사항 가져오기
	            noticePage = noticesRepository.findByCategory(category, pageable);
	        }

	        return noticePage.map(Notice::toDto);
	    }
}
