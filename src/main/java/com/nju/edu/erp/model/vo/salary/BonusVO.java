package com.nju.edu.erp.model.vo.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusVO {// 年终奖VO
    /**
     * 员工uid
     */
    private Integer uid;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 前11个月薪资
     */
    private BigDecimal salary;
    /**
     * 年终奖
     */
    private BigDecimal bonus;
}
