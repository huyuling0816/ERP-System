package com.nju.edu.erp.service.strategy.salaryStrategy;

import java.math.BigDecimal;
import java.util.Date;


public interface SalaryCalculateStrategy {
    BigDecimal calculateRawSalary(Integer uid, Date date);

}
