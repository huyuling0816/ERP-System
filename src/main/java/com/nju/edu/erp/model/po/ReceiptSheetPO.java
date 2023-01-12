package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptSheetPO extends SheetPO {
    /**
     * 收款单单据编号（格式为：XSD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 供应商/销售商id
     */
    private Integer supplier;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 总额汇总
     */
    private BigDecimal totalAmount;
    /**
     * 单据状态
     */
    private FinanceSheetState state;
    /**
     * 创建时间
     */
    private Date createTime;
}
