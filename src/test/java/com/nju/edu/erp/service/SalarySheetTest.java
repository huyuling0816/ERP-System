package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.model.po.SalarySheetPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SalarySheetTest {
    @Autowired
    SalaryService salaryService;

    @Autowired
    SalarySheetDao salarySheetDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeAllSalarySheet(){
        salaryService.makeAllSalarySheet();
        SalarySheetPO salarySheetPO = salarySheetDao.getLatestSheet();
        Assertions.assertNotNull(salarySheetPO);
    }
}
