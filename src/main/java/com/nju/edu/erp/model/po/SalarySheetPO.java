package com.nju.edu.erp.model.po;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
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
public class SalarySheetPO {
    /**
     * 销售单单据编号（格式为：GZD-yyyyMMdd-xxxxx)
     */
    private String id;
    /**
     * 员工姓名(可以在已有员工中选择)
     */
    private String uname;
    /**
     * 员工编号(根据员工姓名自动生成)
     */
    private int uid;
    /**
     * 银行账户名字
     */
    private String accountName;
    /**
     * 应发工资
     */
    private BigDecimal rawSalary;
    /**
     * 个人所得税
     */
    private BigDecimal personalIncomeTax;
    /**
     * 失业保险
     */
    private BigDecimal unemploymentInsurance;
    /**
     * 住房公积金
     */
    private BigDecimal housingProvidentFund;
    /**
     * 实发金额
     */
    private BigDecimal actualSalary;
    /**
     * 单据状态
     */
    private SalarySheetState state;

    /**
     * 工资单日期
     */
    private Date date;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;
}
