package com.nju.edu.erp.service.strategy.approvalStrategy;

import com.nju.edu.erp.dao.PaymentSheetDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.PaymentSheetContentPO;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentSheetApproval implements ApprovalStrategy{
    private final PaymentSheetDao paymentSheetDao;
    private final CustomerService customerService;
    private final AccountService accountService;

    @Autowired
    public PaymentSheetApproval(PaymentSheetDao paymentSheetDao, CustomerService customerService, AccountService accountService) {
        this.paymentSheetDao = paymentSheetDao;
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @Override
    public void approval(String sheetId, BaseEnum state) {
        if(state.equals(FinanceSheetState.FAILURE)) {
            PaymentSheetPO paymentSheet = paymentSheetDao.findSheetById(sheetId);
            if(paymentSheet.getState() == FinanceSheetState.SUCCESS)    throw new RuntimeException("状态更新失败");
            int effectLines = paymentSheetDao.updateSheetState(sheetId, (FinanceSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            FinanceSheetState prevState;
            if(state.equals(FinanceSheetState.SUCCESS)) {
                prevState = FinanceSheetState.PENDING;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = paymentSheetDao.updateSheetStateOnPrev(sheetId, prevState, (FinanceSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            // 更新客户表(更新应付字段)
            // 更新应收 receivable
            if(state.equals(FinanceSheetState.SUCCESS)) {
                PaymentSheetPO paymentSheet = paymentSheetDao.findSheetById(sheetId);
                CustomerPO customerPO = customerService.findCustomerById(paymentSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().subtract(paymentSheet.getTotalAmount()));
                if(customerPO.getReceivable().compareTo(BigDecimal.ZERO) < 0){ // 防御式编程
                    customerPO.setReceivable(BigDecimal.ZERO);
                }
                customerService.updateCustomer(customerPO);
            }
            // 更新账户余额
            List<PaymentSheetContentPO> paymentSheetContentPOS = paymentSheetDao.findContentBySheetId(sheetId);
            for(PaymentSheetContentPO paymentSheetContentPO : paymentSheetContentPOS){
                String bankAccount = paymentSheetContentPO.getBankAccount();
                BigDecimal transferAmount = paymentSheetContentPO.getTransferAmount();
                AccountPO accountPO = accountService.findAccountByName(bankAccount);
                accountPO.setAmount(accountPO.getAmount().subtract(transferAmount));
                accountService.updateAmount(accountPO);
            }
        }
    }
}
