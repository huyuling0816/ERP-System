package com.nju.edu.erp.model.vo.salary;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
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
public class SalarySheetVO {

    /**
     * 销售单单据编号（格式为：GZD-yyyyMMdd-xxxxx), 新建单据时前端传null
     */
    private String id;
    /**
     * 员工姓名(可以在已有员工中选择)
     */
    private String uname;
    /**
     * 员工编号(根据员工姓名自动生成), 新建单据时前端传null
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
     * 实发金额 新建单据时前端传null
     */
    private BigDecimal actualSalary;
    /**
     * 单据状态, 新建单据时前端传null
     */
    private SalarySheetState state;

    /**
     * 工资单日期
     */
    private String dateStr;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;
}
