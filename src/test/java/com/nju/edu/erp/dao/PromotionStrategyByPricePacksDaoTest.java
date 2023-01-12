package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyByPricePacksPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class PromotionStrategyByPricePacksDaoTest {
    @Autowired
    PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void save() {
        PromotionStrategyByPricePacksPO promotionStrategyByPricePacksPO = PromotionStrategyByPricePacksPO.builder()
                .reducePrice(BigDecimal.valueOf(100))
                .number1(1)
                .number2(1)
                .sid("CXCLP-20220709-00000")
                .pid1("0000000000400000")
                .pid2("0000000000400001")
                .build();

        promotionStrategyByPricePacksDao.saveStrategy(promotionStrategyByPricePacksPO);
        PromotionStrategyByPricePacksPO latestSheet = promotionStrategyByPricePacksDao.getLatestSheet();
        Assertions.assertEquals(0, BigDecimal.valueOf(100).compareTo(latestSheet.getReducePrice()));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAll() {
        List<PromotionStrategyByPricePacksPO> all = promotionStrategyByPricePacksDao.findAll();
        Assertions.assertEquals(1, all.size());
    }
}
