package com.nju.edu.erp.service;


import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface SalaryCalculateService {
    /**
     *  计算薪资、税率等，返回有基本信息的工资单，之后调用SalaryService
     * @param uid
     * @return
     */
    SalarySheetVO makeSimpleSalarySheet(Integer uid, Date date);

}
