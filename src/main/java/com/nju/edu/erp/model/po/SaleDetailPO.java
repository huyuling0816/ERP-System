package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDetailPO {
    /**
     * 销售操作类型，销售或销售退货
     */
    private String saleType;

    /**
     * 销售单/销售退货单id
     */
    private String sheetId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 型号
     */
    private String productType;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总额
     */
    private BigDecimal totalPrice;

    /**
     * 销售单/销售退货单创建时间
     */
    private Date createTime;

}
