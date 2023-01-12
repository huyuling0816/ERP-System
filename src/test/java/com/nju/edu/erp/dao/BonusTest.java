package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.BonusPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class BonusTest {
    @Autowired
    BonusDao bonusDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void create() {
        BonusPO bonusPO = BonusPO.builder()
                .uid(1)
                .year(2022)
                .salary(BigDecimal.valueOf(10000))
                .bonus(BigDecimal.valueOf(2000))
                .build();
        int res = bonusDao.createBonus(bonusPO);
        Assertions.assertEquals(1, res);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void getAll() {
        List<BonusPO> bonusList = bonusDao.getBonusList();
        Assertions.assertEquals(true, (bonusList.size() - 0) > 0 );
    }
}
