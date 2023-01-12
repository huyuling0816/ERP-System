package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AccountPO;
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
public class AccountTest {
    @Autowired
    AccountDao accountDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void add() {
        AccountPO accountPO = AccountPO.builder()
                .name("88888")
                .amount(BigDecimal.valueOf(100.33))
                .build();
        accountDao.addAccount(accountPO);
        AccountPO latestAccount = accountDao.getLatestAccount();
        Assertions.assertEquals(latestAccount, accountPO);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void delete() {
        AccountPO accountPO = AccountPO.builder()
                .name("88888")
                .amount(BigDecimal.valueOf(100.33))
                .build();
        accountDao.addAccount(accountPO);
        accountDao.deleteByName("88888");
        AccountPO latestAccount = accountDao.getLatestAccount();
        Assertions.assertEquals(latestAccount.equals(accountPO), false);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void update() {
        AccountPO accountPO = AccountPO.builder()
                .name("88888")
                .amount(BigDecimal.valueOf(100.33))
                .build();
        accountDao.addAccount(accountPO);
        accountPO.setAmount(BigDecimal.valueOf(99.33));
        accountDao.updateOne(accountPO);
        AccountPO latestAccount = accountDao.getLatestAccount();
        Assertions.assertEquals(latestAccount.getAmount().compareTo(BigDecimal.valueOf(99.33)),  0);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAll() {
        List<AccountPO> all = accountDao.findAll();
        Assertions.assertEquals(all.size(), 6);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void findByKeyword() {
        List<AccountPO> byKeyword = accountDao.findByKeyword("5005");
        List<String> names = new ArrayList<>();
        names.add("201250057");
        names.add("201250056");
        names.add("20125005");
        names.add("15005");
        int num = 0;
        for(AccountPO accountPO : byKeyword) {
            if (names.indexOf(accountPO.getName()) != -1) {
                num ++;
            }
        }
        Assertions.assertEquals(num, 4);
    }
}
