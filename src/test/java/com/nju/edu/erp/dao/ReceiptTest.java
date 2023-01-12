package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.ReceiptSheetContentPO;
import com.nju.edu.erp.model.po.ReceiptSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetContentVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
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
public class ReceiptTest {
    @Autowired
    ReceiptSheetDao receiptSheetDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void create() {
        ReceiptSheetPO receiptSheetPO = ReceiptSheetPO.builder()
                .id("SKD-20220709-00000")
                .operator("cyx")
                .totalAmount(BigDecimal.valueOf(1000))
                .state(FinanceSheetState.PENDING)
                .supplier(1)
                .build();
        int res = receiptSheetDao.saveSheet(receiptSheetPO);
        Assertions.assertEquals(1, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void saveContent() {
        ReceiptSheetContentPO receiptSheetContentPO = ReceiptSheetContentPO.builder()
                .receiptSheetId("SKD-20220611-00000")
                .bankAccount("201250057")
                .transferAmount(BigDecimal.valueOf(1000))
                .remark("test").build();

        List<ReceiptSheetContentPO> list = new ArrayList<>();
        list.add(receiptSheetContentPO);
        int res = receiptSheetDao.saveBatchSheetContent(list);
        Assertions.assertEquals(1, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findByState() {
        List<ReceiptSheetPO> allByState = receiptSheetDao.findAllByState(FinanceSheetState.SUCCESS);
        Assertions.assertEquals(2, allByState.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void update() {
        ReceiptSheetPO sheetById = receiptSheetDao.findSheetById("SKD-20220627-00002");
        Assertions.assertEquals(FinanceSheetState.PENDING, sheetById.getState());

        receiptSheetDao.updateSheetState("SKD-20220627-00002", FinanceSheetState.SUCCESS);


        ReceiptSheetPO sheetById1 = receiptSheetDao.findSheetById("SKD-20220627-00002");
        Assertions.assertEquals(FinanceSheetState.SUCCESS, sheetById1.getState());
    }
}
