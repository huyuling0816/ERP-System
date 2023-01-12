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
public class IncomeAfterDiscount {
    /**
     * 销售收入
     */
    BigDecimal saleIncome;

    /**
     * 商品报溢收入
     */
    BigDecimal commodityOverflowIncome;

    /**
     * 成本调价收入
     */
    BigDecimal costAdjustmentIncome;

    /**
     * 进货退货差价
     */
    BigDecimal saleReturnDiffIncome;

    /**
     * 代金券与实际收款差额收入
     */
    BigDecimal voucherDiffIncome;

    BigDecimal totalIncome;
}
