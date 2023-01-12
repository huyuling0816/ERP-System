package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.PaymentSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetContentVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.service.strategy.approvalStrategy.PaymentSheetApproval;
import com.nju.edu.erp.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentSheetDao paymentSheetDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PaymentSheetApproval paymentSheetApproval;


    @Test
    @Transactional
    @Rollback(value = true)
    public void makePaymentSheet(){
        UserVO userVO = UserVO.builder()
                .name("ccyyxx")
                .role(Role.FINANCIAL_STAFF)
                .build();

        List<PaymentSheetContentVO> paymentSheetContentVOS = new ArrayList<>();
        paymentSheetContentVOS.add(PaymentSheetContentVO.builder()
                .bankAccount("201250057666")
                .transferAmount(BigDecimal.valueOf(3000000))
                .remark("我收到钱了")
                .build());
        paymentSheetContentVOS.add(PaymentSheetContentVO.builder()
                .bankAccount("201250056666")
                .transferAmount(BigDecimal.valueOf(2000000))
                .remark("再收一点")
                .build());
        PaymentSheetVO paymentSheetVO = PaymentSheetVO.builder()
                .paymentSheetContent(paymentSheetContentVOS)
                .supplier(2)
                .build();
        PaymentSheetPO prevSheet = paymentSheetDao.getLatestSheet();
        String realSheetId = IdGenerator.generateSheetId(prevSheet == null ? null : prevSheet.getId(), "FKD");

        paymentService.makePaymentSheet(userVO, paymentSheetVO);
        PaymentSheetPO latestSheet = paymentSheetDao.getLatestSheet();
        CustomerPO customerPO = customerDao.findOneById(2);
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId, latestSheet.getId());
        Assertions.assertEquals(0,customerPO.getReceivable().compareTo(BigDecimal.valueOf(11151000.00)));
        Assertions.assertEquals(FinanceSheetState.PENDING, latestSheet.getState());
    }

    @Transactional
    @Test
    @Rollback(value = true)
    public void approval_1() { // 测试审批
        // 审批成功后修改客户应收金额
        paymentSheetApproval.approval("FKD-20220611-00000", FinanceSheetState.SUCCESS);
        PaymentSheetPO paymentSheetPO = paymentSheetDao.findSheetById("FKD-20220611-00000");
        Assertions.assertEquals(FinanceSheetState.SUCCESS, paymentSheetPO.getState());

        CustomerPO customerPO = customerDao.findOneById(2);
        Assertions.assertEquals(0, customerPO.getReceivable().compareTo(BigDecimal.valueOf(6151000.00)));


        // 审批成功后修改账户余额
        AccountPO accountPO = accountDao.findByName("201250056666");
        Assertions.assertEquals(0, accountPO.getAmount().compareTo(BigDecimal.valueOf(3000000.00)));

    }
}
