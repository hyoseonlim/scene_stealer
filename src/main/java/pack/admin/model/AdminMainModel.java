package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.ActorDto;
import pack.dto.CharacterDto;
import pack.dto.ShowActorDto;
import pack.dto.ShowDto;
import pack.entity.Actor;
import pack.entity.Show;
import pack.repository.ActorsRepository;
import pack.repository.CharactersRepository;
import pack.repository.ShowActorRepository;
import pack.repository.ShowsRepository;

@Repository
public class AdminMainModel {
		@Autowired
		ShowsRepository showsRepo;
		
		@Autowired
		ActorsRepository actorsRepo;
		
		@Autowired
		ShowActorRepository showActorRepo;
		
		@Autowired
		CharactersRepository charactersRepo;
		
		public List<ShowDto> searchShows() {
	        return showsRepo.findAll().stream().map(Show::toDto).toList();
	    }
		
		// 작품명 자동완성
		public List<ShowDto> searchShows(String term) {
	        return showsRepo.findByTitleContaining(term).stream().map(Show::toDto).toList();
	    }
		
		// 작품 추가
		public int insertShow(ShowDto dto) {
			 // 엔티티를 저장하고, 저장된 엔티티를 반환받음
	        Show showentity = showsRepo.save(ShowDto.toEntity(dto));
	        // 자동 생성된 ID를 반환
	        return showentity.getNo();
		}
		
		// 배우 추가
		public int insertActor(String actorName) {
			ActorDto actordto = new ActorDto();
			actordto.setName(actorName);
			// 엔티티를 저장하고, 저장된 엔티티를 반환받음
	        Actor actorentity = actorsRepo.save(ActorDto.toEntity(actordto));
	        // 자동 생성된 ID를 반환
	        return actorentity.getNo();
		}
		
		// 작품-배우 관계 추가
		public void insertShowActor(int showNo, int actorNo) {
			ShowActorDto dto = new ShowActorDto();
			dto.setShowNo(showNo);
			dto.setActorNo(actorNo);
			showActorRepo.save(ShowActorDto.toEntity(dto));
		}
		
		// 배역 추가
		public void insertCharacter(CharacterDto dto) {
			charactersRepo.save(CharacterDto.toEntity(dto));
		}
		
}
