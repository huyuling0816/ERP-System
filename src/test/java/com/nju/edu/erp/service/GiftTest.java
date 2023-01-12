package com.nju.edu.erp.service;
import com.nju.edu.erp.dao.GiftSheetDao;
import java.util.List;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.GiftSheetPO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.promotionStrategy.GiftSheetVO;
import com.nju.edu.erp.service.strategy.approvalStrategy.GiftSheetApproval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class GiftTest {

    @Autowired
    GiftSheetService giftSheetService;

    @Autowired
    GiftSheetApproval giftSheetApproval;

    @Autowired
    GiftSheetDao giftSheetDao;

    @Autowired
    ProductService productService;

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeGiftSheet() {
        GiftSheetPO giftSheetPO = GiftSheetPO.builder()
                .pid("0000000000400001")
                .number(10)
                .saleSheetId("XSD-20220701-00004")
                .build();
        giftSheetService.makeGiftSheet(giftSheetPO);
        GiftSheetPO latest = giftSheetDao.getLatestSheet();
        Assertions.assertEquals(0,latest.getPid().compareTo("0000000000400001"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getGiftSheetByState(){
        List<GiftSheetVO> giftSheetByState = giftSheetService.getGiftSheetByState(GiftSheetState.PENDING);
        Assertions.assertNotNull(giftSheetByState);
        Assertions.assertEquals(1, giftSheetByState.size());
        GiftSheetVO sheet1 = giftSheetByState.get(0);
        Assertions.assertNotNull(sheet1);
        Assertions.assertEquals("ZSD-20220706-00000",sheet1.getId());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval() {
        ProductInfoVO productInfoVO = productService.getOneProductByPid("0000000000400001");
        giftSheetApproval.approval("ZSD-20220706-00000",GiftSheetState.SUCCESS);
        GiftSheetPO sheet = giftSheetDao.findSheetById("ZSD-20220706-00000");
        Assertions.assertEquals(GiftSheetState.SUCCESS,sheet.getState());
    }
}
