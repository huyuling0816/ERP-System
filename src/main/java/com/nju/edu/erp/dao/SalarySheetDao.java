package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Repository
@Mapper
public interface SalarySheetDao {

    int createSalarySheet(SalarySheetPO salarySheetPO);

    SalarySheetPO getLatestSheet();

    List<SalarySheetPO> getSalarySheetByState(SalarySheetState state);

    List<SalarySheetPO> getAllSalarySheet();

    SalarySheetPO getSheetById(String id);

    int updateSheetState(String id,SalarySheetState state);

    int updateSheetStateOnPrev(String id, SalarySheetState prev, SalarySheetState state);

    /**
     * 计算员工前11个月的工资，算年终奖
     * @param uid
     * @return
     */
    BigDecimal getYearActualSalary(Integer uid, Date date);

}
