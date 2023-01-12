package com.nju.edu.erp.service.strategy.salaryStrategy;


import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import com.nju.edu.erp.service.PersonnelService;
import com.nju.edu.erp.service.SalarySystemService;

import java.math.BigDecimal;
import java.util.Date;

public class YearlyStrategy implements SalaryCalculateStrategy{
    private final SalarySystemService salarySystemService;
    private final PersonnelService personnelService;



    public YearlyStrategy(SalarySystemService salarySystemService,PersonnelService personnelService){
        this.salarySystemService=salarySystemService;
        this.personnelService=personnelService;
    }


    @Override
    public BigDecimal calculateRawSalary(Integer uid, Date date){
        //总经理不用打卡
        PersonnelVO personnelVO=personnelService.findById(uid);
        SalarySystemVO salarySystemVO=salarySystemService.findSalarySystem(personnelVO.getRole());
        return salarySystemVO.getBase().add(salarySystemVO.getPost());
    }
}
