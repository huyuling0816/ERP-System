package com.nju.edu.erp.service.strategy.promotionStrategy;

import com.nju.edu.erp.dao.PromotionStrategyByPricePacksDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.model.po.PromotionStrategyByPricePacksPO;
import com.nju.edu.erp.model.po.SaleSheetContentPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PromotionStrategyByPricePacks implements PromotionStrategy {
    private final PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao;
    private final SaleSheetDao saleSheetDao;

    @Autowired
    public PromotionStrategyByPricePacks(PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao,
                                         SaleSheetDao saleSheetDao) {
        this.promotionStrategyByPricePacksDao = promotionStrategyByPricePacksDao;
        this.saleSheetDao = saleSheetDao;
    }

    public void updateSaleSheet(SaleSheetPO saleSheetPO){
        Date createTime = saleSheetPO.getCreateTime();

        // 计算商品名和数量
        HashMap<String, Integer> productMap = new HashMap<>();
        List<SaleSheetContentPO> saleSheetContentPOList = saleSheetDao.findContentBySheetId(saleSheetPO.getId());
        saleSheetContentPOList.forEach(saleSheetContentPO -> {
            productMap.put(saleSheetContentPO.getPid(), saleSheetContentPO.getQuantity());
        });

        // 按促销策略计算得到的代金券
        BigDecimal totalReduce = BigDecimal.ZERO;

        List<PromotionStrategyByPricePacksPO> allStrategy = promotionStrategyByPricePacksDao.findAll();
        for (PromotionStrategyByPricePacksPO promotionStrategyByPricePacksPO: allStrategy) {
            Date beginTime = promotionStrategyByPricePacksPO.getBeginTime();
            Date endTime = promotionStrategyByPricePacksPO.getEndTime();
            // 不是有效时间
            if (createTime.compareTo(beginTime) < 0 || createTime.compareTo(endTime) > 0) {
                continue;
            }

            String pid1 = promotionStrategyByPricePacksPO.getPid1();
            String pid2 = promotionStrategyByPricePacksPO.getPid2();
            Integer number1 = promotionStrategyByPricePacksPO.getNumber1();
            Integer number2 = promotionStrategyByPricePacksPO.getNumber2();
            BigDecimal reducePrice = promotionStrategyByPricePacksPO.getReducePrice();

            // 计算代金券
            Integer tempNum1 = productMap.get(pid1);
            Integer tempNum2 = productMap.get(pid2);
            if (tempNum1 == null || tempNum2 == null) {
                return;
            }
            while (tempNum1 >= number1 && tempNum2 >= number2) {
                tempNum1 -= number1;
                tempNum2 -= number2;
                totalReduce = totalReduce.add(reducePrice);
            }
            productMap.replace(pid1, tempNum1);
            productMap.replace(pid2, tempNum2);
        }

        // 更新销售单的代金券
        // saleSheetPO.setRawTotalAmount(saleSheetPO.getRawTotalAmount().subtract(totalReduce));
        saleSheetPO.setVoucherAmount(saleSheetPO.getVoucherAmount().add(totalReduce));
    }
}
