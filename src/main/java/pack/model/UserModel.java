package pack.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import pack.dto.NoticeDto;
import pack.entity.Notice;
import pack.repository.NoticesRepository;

@Repository
public class UserModel {
	
	@Autowired
	private NoticesRepository nrps;
	
	public Page<NoticeDto> getNoticeList(Pageable pageable) {
		Page<Notice> noticePage = nrps.findAll(pageable);
		return noticePage.map(Notice::toDto);
	}
	
	public NoticeDto getNoticeInfo(int noticeNo) {
		return Notice.toDto(nrps.findById(noticeNo).get());
	}

}
