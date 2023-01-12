package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ReceiptSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ReceiptSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetContentVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import com.nju.edu.erp.service.strategy.approvalStrategy.ReceiptSheetApproval;
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
public class ReceiptServiceTest {
    @Autowired
    ReceiptService receiptService;

    @Autowired
    ReceiptSheetDao receiptSheetDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    ReceiptSheetApproval receiptSheetApproval;


    @Test
    @Transactional
    @Rollback(value = true)
    public void makeReceiptSheet(){
        UserVO userVO = UserVO.builder()
                .name("chenyixiao")
                .role(Role.FINANCIAL_STAFF)
                .build();

        List<ReceiptSheetContentVO> receiptSheetContentVOS = new ArrayList<>();
        receiptSheetContentVOS.add(ReceiptSheetContentVO.builder()
                .bankAccount("201250057")
                .transferAmount(BigDecimal.valueOf(3000))
                .remark("Test-sheet1")
                .build());
        receiptSheetContentVOS.add(ReceiptSheetContentVO.builder()
                .bankAccount("201250056")
                .transferAmount(BigDecimal.valueOf(5100))
                .remark("Test-sheet2")
                .build());
        ReceiptSheetVO receiptSheetVO = ReceiptSheetVO.builder()
                .receiptSheetContent(receiptSheetContentVOS)
                .supplier(1)
                .build();
        ReceiptSheetPO prevSheet = receiptSheetDao.getLatestSheet();
        String realSheetId = IdGenerator.generateSheetId(prevSheet == null ? null : prevSheet.getId(), "SKD");

        receiptService.makeReceiptSheet(userVO, receiptSheetVO);
        ReceiptSheetPO latestSheet = receiptSheetDao.getLatestSheet();
        CustomerPO customerPO = customerDao.findOneById(1);
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId, latestSheet.getId());
        Assertions.assertEquals(-1,customerPO.getPayable().compareTo(BigDecimal.valueOf(8700000.00)));
        Assertions.assertEquals(FinanceSheetState.PENDING, latestSheet.getState());
    }

    @Transactional
    @Test
    @Rollback(value = true)
    public void approval_1() { // 测试审批
        // 审批成功后修改客户应付金额
        receiptSheetApproval.approval("SKD-20220627-00002", FinanceSheetState.SUCCESS);
        ReceiptSheetPO receiptSheetPO = receiptSheetDao.findSheetById("SKD-20220627-00002");
        Assertions.assertEquals(FinanceSheetState.SUCCESS, receiptSheetPO.getState());

        CustomerPO customerPO = customerDao.findOneById(1);
        Assertions.assertEquals(0, customerPO.getPayable().compareTo(BigDecimal.valueOf(8691900.00)));

        // 审批成功后修改账户余额
        AccountPO accountPO = accountDao.findByName("201250057");
        Assertions.assertEquals(0, accountPO.getAmount().compareTo(BigDecimal.valueOf(97000)));

    }
}
