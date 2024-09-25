package pack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReturnRateDto {
    private Integer productNo;
    private String productName;
    private Integer deliveredQuantity;
    private Integer canceledQuantity;
    private Double returnRate;
}

