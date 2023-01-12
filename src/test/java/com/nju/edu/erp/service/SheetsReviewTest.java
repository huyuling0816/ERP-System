package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.SaleDetailPO;
import com.nju.edu.erp.model.vo.OperationSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class SheetsReviewTest {
    @Autowired
    SheetsReviewService sheetsReviewService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    SaleService saleService;

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByTime_1() {
        // 有记录的情况
        try {
            List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByTime("2021-05-23 23:17:40","2022-05-24 00:33:12");
            Assertions.assertEquals(saleDetailPOList.size(),4);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByTime_2() {
        // 结束时间大于开始时间
        try {
            List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByTime("2023-05-23 23:17:40","2022-05-24 00:33:12");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByProduct_1() {
        // 有记录的情况
        List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByProduct("戴尔电脑");
        Assertions.assertEquals(saleDetailPOList.size(),5);
    }

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByCustomer_1() {
        // 有记录的情况
        List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByCustomer("lxs");
        Assertions.assertEquals(saleDetailPOList.size(),8);
    }

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByOperator_1() {
        // 有记录的情况
        List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByOperator("xiaoshoujingli");
        Assertions.assertEquals(saleDetailPOList.size(),8);
    }

    @Test
    @Transactional
    @Rollback
    public void getSaleDetailByWarehouse_1() {
        // 有记录的情况
        List<SaleDetailPO> saleDetailPOList = sheetsReviewService.getSaleDetailByWarehouse("0000000000400000");
        Assertions.assertEquals(saleDetailPOList.size(),25);
    }

    @Test
    @Transactional
    @Rollback
    public void getOperationSheet_1() {
        OperationSheetVO operationSheet = sheetsReviewService.getOperationSheet();
        Assertions.assertEquals(operationSheet.getIncomeAfterDiscount().compareTo(BigDecimal.valueOf(40251400.00)),1);
        Assertions.assertEquals(operationSheet.getTotalIncomeDiscount().compareTo(BigDecimal.valueOf(1108600.00)), 0);
        Assertions.assertEquals(operationSheet.getTotalExpenditure().compareTo(BigDecimal.valueOf(11130000.00)), 1);
        Assertions.assertEquals(operationSheet.getTotalProfit().compareTo(BigDecimal.valueOf(29121400.00)), 1);
    }

    @Test
    @Transactional
    @Rollback
    public void getSheetByTime_1() {
        List<SheetVO> sheetByTime = sheetsReviewService.getSheetByTime("2021-05-23 23:17:40", "2022-06-28 00:33:12");
        Assertions.assertEquals(sheetByTime.size(), 32);
    }
    @Test
    @Transactional
    @Rollback
    public void getSheetBySheetType_1() {
        List<SheetVO> sheetBySheetType = sheetsReviewService.getSheetBySheetType("销售出货单");
        Assertions.assertEquals(sheetBySheetType.size(),8);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void redFlush_1() {
        SaleSheetVO saleSheet = saleService.getSaleSheetById("XSD-20220524-00000");
        sheetsReviewService.redFlush(saleSheet);
        SaleSheetVO redFlushSaleSheet = saleService.getSaleSheetById("XSD-20220709-00000");
        Assertions.assertEquals(redFlushSaleSheet.getFinalAmount().add(saleSheet.getFinalAmount()).compareTo(BigDecimal.ZERO), 0);
    }

    @Test
    @Transactional
    @Rollback
    public void getSheetByCustomerName_1() {
        List<SheetVO> sheetByCustomerName = sheetsReviewService.getSheetByCustomerName("lxs");
        Assertions.assertEquals(sheetByCustomerName.size(), 13);
    }

    @Test
    @Transactional
    @Rollback
    public void getSheetByOperatorName_1() {
        List<SheetVO> sheetByOperatorName = sheetsReviewService.getSheetByOperatorName("xiaoshoujingli");
        Assertions.assertEquals(sheetByOperatorName.size(), 20);
    }
}
