package com.nju.edu.erp.service.strategy.approvalStrategy;

import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalarySheetApproval implements ApprovalStrategy{
    private final SalarySheetDao salarySheetDao;
    private final AccountService accountService;

    @Autowired
    public SalarySheetApproval(SalarySheetDao salarySheetDao, AccountService accountService) {
        this.salarySheetDao = salarySheetDao;
        this.accountService = accountService;
    }

    @Override
    public void approval(String sheetId, BaseEnum state) {
        if(state.equals(SalarySheetState.FAILURE)) {
            SalarySheetPO salarySheetPO = salarySheetDao.getSheetById(sheetId);
            if(salarySheetPO.getState() == SalarySheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = salarySheetDao.updateSheetState(sheetId, (SalarySheetState) state);
            if(effectLines==0) throw new RuntimeException("状态更新失败");
        }else{
            SalarySheetState prevState;
            if(state.equals(SalarySheetState.SUCCESS)) {
                prevState = SalarySheetState.PENDING;
            }else{
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = salarySheetDao.updateSheetStateOnPrev(sheetId,prevState, (SalarySheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            // 更新account
            if(state.equals(SalarySheetState.SUCCESS)){
                SalarySheetPO salarySheetPO = salarySheetDao.getSheetById(sheetId);
                String accountName = salarySheetPO.getAccountName();
                AccountPO accountPO = accountService.findAccountByName(accountName);
                if(accountPO == null) throw new RuntimeException("此账户不存在");
                accountPO.setAmount(accountPO.getAmount().add(salarySheetPO.getActualSalary()));
                accountService.updateAmount(accountPO);
            }
        }
    }
}
