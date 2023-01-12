package com.nju.edu.erp.model.vo.operationSheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expenditure {
    /**
     * 销售成本，即进价
     */
    BigDecimal purchasePrice;

    /**
     * 商品报损
     */
    BigDecimal commodityLoss;

    /**
     * 商品赠送
     */
    BigDecimal giftAmount;

    /**
     * 人力成本
     */
    BigDecimal humanResourceAmount;

    BigDecimal totalExpenditure;
}
