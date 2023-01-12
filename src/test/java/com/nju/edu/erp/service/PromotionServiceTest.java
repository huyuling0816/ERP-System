package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByPricePacksVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByTotalPriceVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByUserLevelVO;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import com.nju.edu.erp.model.vo.sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategy;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByPricePacks;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByTotalPrice;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByUserLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PromotionServiceTest {

    @Autowired
    PromotionService promotionService;
    @Autowired
    GiftSheetService giftSheetService;
    @Autowired
    SaleService saleService;
    @Autowired
    PromotionStrategyByUserLevelDao promotionStrategyByUserLevelDao;
    @Autowired
    PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao;
    @Autowired
    PromotionStrategyByTotalPriceDao promotionStrategyByTotalPriceDao;
    @Autowired
    SaleSheetDao saleSheetDao;
    @Autowired
    GiftSheetDao giftSheetDao;
    @Autowired
    PromotionStrategyByUserLevel promotionStrategyByUserLevel;
    @Autowired
    PromotionStrategyByPricePacks promotionStrategyByPricePacks;
    @Autowired
    PromotionStrategyByTotalPrice promotionStrategyByTotalPrice;

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeStrategyByUserLevel() { // 测试策略是否生成成功
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<PromotionStrategyByUserLevelVO> vos = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                PromotionStrategyByUserLevelVO vo = PromotionStrategyByUserLevelVO.builder()
                        .level(i)
                        .discount(BigDecimal.valueOf(0.9))
                        .voucherAmount(BigDecimal.valueOf(200))
                        .gid("0000000000400000")
                        .numOfGift(2)
                        .beginTime(dateFormat.parse("2022-07-01 23:59:59"))
                        .endTime(dateFormat.parse("2022-07-10 23:59:59"))
                        .build();
                vos.add(vo);
            }
            promotionService.makePromotionStrategyByUserLevel(vos);
            int numOfRecords = promotionStrategyByUserLevelDao.numOfRecords();
            Assertions.assertEquals(20, numOfRecords);
            PromotionStrategyByUserLevelPO latest = promotionStrategyByUserLevelDao.getLatestStrategy();
            Assertions.assertEquals(0, latest.getGid().compareTo("0000000000400000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeStrategyByUserLevel_2() { // 测试策略是否生成成功
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<PromotionStrategyByUserLevelVO> vos = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                PromotionStrategyByUserLevelVO vo = PromotionStrategyByUserLevelVO.builder()
                        .level(i)
                        .discount(null)
                        .voucherAmount(null)
                        .gid(null)
                        .numOfGift(null)
                        .beginTime(dateFormat.parse("2022-07-01 23:59:59"))
                        .endTime(dateFormat.parse("2022-07-10 23:59:59"))
                        .build();
                vos.add(vo);
            }
            promotionService.makePromotionStrategyByUserLevel(vos);
            int numOfRecords = promotionStrategyByUserLevelDao.numOfRecords();
            Assertions.assertEquals(20, numOfRecords);
            PromotionStrategyByUserLevelPO latest = promotionStrategyByUserLevelDao.getLatestStrategy();
            Assertions.assertEquals(0, latest.getGid().compareTo("0000000000400000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateSaleSheet_1() {
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .build();
        List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400000")
                .quantity(50)
                .remark("Test1-product1")
                .unitPrice(BigDecimal.valueOf(3200))
                .build());
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400001")
                .quantity(60)
                .remark("Test1-product2")
                .unitPrice(BigDecimal.valueOf(4200))
                .build());
        SaleSheetVO saleSheetVO = SaleSheetVO.builder()
                .saleSheetContent(saleSheetContentVOS)
                .supplier(2)
                .discount(BigDecimal.valueOf(0.8))
                .voucherAmount(BigDecimal.valueOf(300))
                .remark("Test1")
                .build();
        List<PromotionStrategy> promotionStrategyList = new ArrayList<>();
        promotionStrategyList.add(promotionStrategyByTotalPrice);
        promotionStrategyList.add(promotionStrategyByPricePacks);
        promotionStrategyList.add(promotionStrategyByUserLevel);
        saleService.makeSaleSheet(userVO, saleSheetVO, promotionStrategyList);
        SaleSheetPO latestSheet = saleSheetDao.getLatestSheet();
        promotionStrategyByUserLevel.updateSaleSheet(latestSheet);
        Assertions.assertEquals(-1, latestSheet.getDiscount().compareTo(BigDecimal.valueOf(0.90)));
        Assertions.assertEquals(1, latestSheet.getVoucherAmount().compareTo(BigDecimal.valueOf(700)));
        GiftSheetPO gift = giftSheetDao.getLatestSheet();
        Assertions.assertEquals(0, gift.getPid().compareTo("0000000000400000"));
        Assertions.assertEquals(0, gift.getNumber().compareTo(2));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeStrategyByPack() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            PromotionStrategyByPricePacksVO packsVO = PromotionStrategyByPricePacksVO.builder()
                    .beginTime(dateFormat.parse("2022-07-01 23:59:59"))
                    .endTime(dateFormat.parse("2022-07-10 23:59:59"))
                    .pid1("0000000000400000")
                    .number1(1)
                    .pid2("0000000000400001")
                    .number2(2)
                    .reducePrice(BigDecimal.valueOf(500))
                    .build();
            promotionService.makePromotionStrategyByPricePacks(packsVO);
            List<PromotionStrategyByPricePacksPO> all = promotionStrategyByPricePacksDao.findAll();
            Assertions.assertEquals(all.size(), 2);
            Assertions.assertEquals(all.get(0).getEndTime(), dateFormat.parse("2022-07-10 23:59:59"));
            Assertions.assertEquals(all.get(0).getReducePrice().compareTo(BigDecimal.valueOf(500)), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateSaleSheet_2() {
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .build();
        List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400000")
                .quantity(50)
                .remark("Test1-product1")
                .unitPrice(BigDecimal.valueOf(3000))
                .build());
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400001")
                .quantity(60)
                .remark("Test1-product2")
                .unitPrice(BigDecimal.valueOf(4200))
                .build());
        SaleSheetVO saleSheetVO = SaleSheetVO.builder()
                .saleSheetContent(saleSheetContentVOS)
                .supplier(2)
                .discount(BigDecimal.valueOf(0.8))
                .voucherAmount(BigDecimal.valueOf(300))
                .remark("Test1")
                .build();

        List<PromotionStrategy> promotionStrategyList = new ArrayList<>();
        promotionStrategyList.add(promotionStrategyByTotalPrice);
        promotionStrategyList.add(promotionStrategyByPricePacks);
        promotionStrategyList.add(promotionStrategyByUserLevel);
        saleService.makeSaleSheet(userVO, saleSheetVO, promotionStrategyList);
        SaleSheetPO latestSheet = saleSheetDao.getLatestSheet();
        BigDecimal rawTotalAmount = latestSheet.getRawTotalAmount();
        promotionStrategyByPricePacks.updateSaleSheet(latestSheet);
        BigDecimal rawAmountAfterPromotion = latestSheet.getRawTotalAmount();
        BigDecimal subtract = rawTotalAmount.subtract(rawAmountAfterPromotion);
        Assertions.assertEquals(BigDecimal.valueOf(500).multiply(BigDecimal.valueOf(30)).compareTo(subtract), 1);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void makeStrategyByTotalPrice() { // 测试策略是否生成成功
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PromotionStrategyByTotalPriceVO> vos = new ArrayList<>();
        Integer[] totalPrice = {999, 1999, 4999, 19999, 99999};
        Integer[] voucher = {19, 39, 139, 599, 3999};

        try {
            for (int i = 0; i < 5; i++) {
                PromotionStrategyByTotalPriceVO vo = PromotionStrategyByTotalPriceVO.builder()
                        .totalPrice(BigDecimal.valueOf(totalPrice[i]))
                        .voucherAmount(BigDecimal.valueOf(voucher[i]))
                        .gid("0000000000400000")
                        .numOfGift(2)
                        .beginTime(dateFormat.parse("2022-07-01 23:59:59"))
                        .endTime(dateFormat.parse("2022-07-10 23:59:59"))
                        .build();
                vos.add(vo);
            }
            promotionService.makePromotionStrategyByTotalPrice(vos);
            int numOfRecords = promotionStrategyByTotalPriceDao.numOfRecords();
            Assertions.assertEquals(10, numOfRecords);
            PromotionStrategyByTotalPricePO latest = promotionStrategyByTotalPriceDao.getLatestStrategy();
            Assertions.assertEquals(0, latest.getGid().compareTo("0000000000400000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateSaleSheet_3() {
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .build();
        List<SaleSheetContentVO> saleSheetContentVOS = new ArrayList<>();
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400000")
                .quantity(50)
                .remark("Test1-product1")
                .unitPrice(BigDecimal.valueOf(3000))
                .build());
        saleSheetContentVOS.add(SaleSheetContentVO.builder()
                .pid("0000000000400001")
                .quantity(60)
                .remark("Test1-product2")
                .unitPrice(BigDecimal.valueOf(4200))
                .build());
        SaleSheetVO saleSheetVO = SaleSheetVO.builder()
                .saleSheetContent(saleSheetContentVOS)
                .supplier(2)
                .discount(BigDecimal.valueOf(0.8))
                .voucherAmount(BigDecimal.valueOf(300))
                .remark("Test1")
                .build();

        List<PromotionStrategy> promotionStrategyList = new ArrayList<>();
        promotionStrategyList.add(promotionStrategyByTotalPrice);
        promotionStrategyList.add(promotionStrategyByPricePacks);
        promotionStrategyList.add(promotionStrategyByUserLevel);
        saleService.makeSaleSheet(userVO, saleSheetVO, promotionStrategyList);
        SaleSheetPO latestSheet = saleSheetDao.getLatestSheet();
        BigDecimal voucher = latestSheet.getVoucherAmount();
        promotionStrategyByTotalPrice.updateSaleSheet(latestSheet);
        BigDecimal voucherPromotion = latestSheet.getVoucherAmount();
        System.out.println(voucher);
        System.out.println(voucherPromotion);
        BigDecimal subtract = voucherPromotion.subtract(voucher);
        Assertions.assertEquals(BigDecimal.valueOf(3999).compareTo(subtract), 0);
    }

    @Test
    public void getAllPromotionStrategyByUserLevel(){
        List<PromotionStrategyByUserLevelVO> vos = promotionService.findAllPromotionStrategyByUserLevel();
        Assertions.assertEquals(15,vos.size());
    }
}
