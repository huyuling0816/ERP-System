package com.nju.edu.erp.model.vo.warehouse;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WarehouseCountingVO {
    /**
     * 库存id
     */
    private Integer id;

    /**
     * 商品编号
     */
    private ProductInfoVO product;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 进价
     */
    private BigDecimal purchasePrice;

    /**
     * 批次
     */
    private Integer batchId;

    /**
     * 出厂日期
     */
    private Date productionDate;
}
