package com.nju.edu.erp.service.strategy.approvalStrategy;

import com.nju.edu.erp.dao.ReceiptSheetDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ReceiptSheetContentPO;
import com.nju.edu.erp.model.po.ReceiptSheetPO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReceiptSheetApproval implements ApprovalStrategy{
    private final ReceiptSheetDao receiptSheetDao;
    private final CustomerService customerService;
    private final AccountService accountService;

    @Autowired
    public ReceiptSheetApproval(ReceiptSheetDao receiptSheetDao, CustomerService customerService, AccountService accountService) {
        this.receiptSheetDao = receiptSheetDao;
        this.customerService = customerService;
        this.accountService = accountService;
    }


    @Override
    public void approval(String sheetId, BaseEnum state) {
        if(state.equals(FinanceSheetState.FAILURE)) {
            ReceiptSheetPO receiptSheet = receiptSheetDao.findSheetById(sheetId);
            if(receiptSheet.getState() == FinanceSheetState.SUCCESS)    throw new RuntimeException("状态更新失败");
            int effectLines = receiptSheetDao.updateSheetState(sheetId, (FinanceSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            FinanceSheetState prevState;
            if(state.equals(FinanceSheetState.SUCCESS)) {
                prevState = FinanceSheetState.PENDING;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = receiptSheetDao.updateSheetStateOnPrev(sheetId, prevState, (FinanceSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            // 更新客户表(更新应付字段)
            // 更新应付 payable
            if(state.equals(FinanceSheetState.SUCCESS)) {
                ReceiptSheetPO receiptSheet = receiptSheetDao.findSheetById(sheetId);
                CustomerPO customerPO = customerService.findCustomerById(receiptSheet.getSupplier());
                customerPO.setPayable(customerPO.getPayable().subtract(receiptSheet.getTotalAmount()));
                if(customerPO.getPayable().compareTo(BigDecimal.ZERO) < 0){ // 防御式编程
                    customerPO.setPayable(BigDecimal.ZERO);
                }
                customerService.updateCustomer(customerPO);
            }
            // 更新账户余额
            List<ReceiptSheetContentPO> receiptSheetContentPOList = receiptSheetDao.findContentBySheetId(sheetId);
            for(ReceiptSheetContentPO receiptSheetContentPO : receiptSheetContentPOList){
                String bankAccount = receiptSheetContentPO.getBankAccount();
                BigDecimal transferAmount = receiptSheetContentPO.getTransferAmount();
                AccountPO accountPO = accountService.findAccountByName(bankAccount);
                accountPO.setAmount(accountPO.getAmount().add(transferAmount));
                accountService.updateAmount(accountPO);
            }
        }
    }
}
