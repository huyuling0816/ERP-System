package com.nju.edu.erp.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationSheetVO {
    /**
     * 折让后总收入
     */
    BigDecimal incomeAfterDiscount;

    /**
     * 总折让额
     */
    BigDecimal totalIncomeDiscount;

    /**
     * 总支出
     */
    BigDecimal totalExpenditure;

    /**
     * 总利润
     */
    BigDecimal totalProfit;
}
