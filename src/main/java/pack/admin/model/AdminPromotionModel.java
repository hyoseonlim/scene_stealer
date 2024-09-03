package pack.admin.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.dto.PopupDto;
import pack.dto.ProductDto;
import pack.entity.Order;
import pack.entity.Popup;
import pack.entity.Product;
import pack.repository.PopupRepository;
import pack.repository.ProductsRepository;

@Repository
public class AdminPromotionModel {
	@Autowired
	private ProductsRepository productRepo;
	
	@Autowired
	private PopupRepository popupRepo;
	
	public List<ProductDto> searchProducts(){
		return productRepo.findAll().stream().map(Product::toDto).toList();
	}
	
	public List<ProductDto> searchProducts(String name){
		return productRepo.findByNameContaining(name).stream().map(Product::toDto).toList();
	}
	
	public void addPopup(PopupDto dto) {
		 popupRepo.save(PopupDto.toEntity(dto)); 
	}
	
	public void updatePopupStatus(Integer no, Boolean status) {
        try {
            Popup entity = popupRepo.findById(no)
                    .orElseThrow(() -> new IllegalStateException("ID가 " + no + "인 팝업을 찾을 수 없습니다."));

            entity.setIsShow(status);  // 상태 업데이트
            popupRepo.save(entity);  // 변경사항 저장
        } catch (Exception e) {
            System.err.println("주문 상태 업데이트 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }
}
