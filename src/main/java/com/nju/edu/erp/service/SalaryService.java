package com.nju.edu.erp.service;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.salary.BonusVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Service
public interface SalaryService {
    void makeSalarySheet(SalarySheetVO salarySheetVO);

    List<SalarySheetVO> getSalaryServiceByState(SalarySheetState state);

    void makeAllSalarySheet();

    List<SalarySheetVO> getAllSalarySheet();

    SalarySheetVO makeSimpleSalarySheet(Integer uid);

    BigDecimal getYearActualSalary(Integer uid, Date date);
}
