package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
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
public class SaleReturnsSheetPO extends SheetPO {
    /**
     * 销售单单据编号（格式为：JHD-yyyyMMdd-xxxxx), 新建单据时前端传null
     */
    private String id;
    /**
     * 关联的销售单id
     */
    private String saleSheetId;
    /**
     * 业务员
     */
    private String salesman;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注
     */
    private String remark;
    /**
     * 折让前总额
     */
    private BigDecimal rawTotalAmount;
    /**
     * 单据状态, 新建单据时前端传null
     */
    private SaleReturnsSheetState state;
    /**
     * 折让后总额, 新建单据时前端传null
     */
    private BigDecimal finalAmount;
    /**
     * 折扣
     */
    private BigDecimal discount;
    /**
     * 使用代金券总额
     */
    private BigDecimal voucherAmount;
    /**
     * 创建时间
     */
    private Date createTime;
}
