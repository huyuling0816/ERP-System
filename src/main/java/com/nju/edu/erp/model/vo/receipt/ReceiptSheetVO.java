package com.nju.edu.erp.model.vo.receipt;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptSheetVO extends SheetVO {
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
     * 单据状态, 新建单据时前端传null
     */
    private FinanceSheetState state;
    /**
     * 转账列表
     */
    List<ReceiptSheetContentVO> receiptSheetContent;

}
