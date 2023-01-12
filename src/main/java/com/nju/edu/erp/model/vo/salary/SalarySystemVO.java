package com.nju.edu.erp.model.vo.salary;


import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.salary.SalaryDistribution;
import com.nju.edu.erp.enums.salary.SalaryMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySystemVO {
    /**
     * 职位
     */
    Role role;

    /**
     * 基本工资
     */
    BigDecimal base;

    /**
     * 岗位工资
     */
    BigDecimal post;

    /**
     * 薪资计算方式
     */
    SalaryMethod salaryMethod;

    /**
     * 发放方式
     */
    SalaryDistribution salaryDistribution;

    /**
     * 失业险比率
     */
    BigDecimal unemploymentInsuranceRate;

    /**
     * 住房公积金比率
     */
    BigDecimal housingFundRate;

}
