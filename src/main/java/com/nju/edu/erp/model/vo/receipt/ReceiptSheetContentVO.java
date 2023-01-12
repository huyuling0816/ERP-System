package com.nju.edu.erp.model.vo.receipt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptSheetContentVO {
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 转账金额
     */
    private BigDecimal transferAmount;
    /**
     * 备注
     */
    private String remark;
}
