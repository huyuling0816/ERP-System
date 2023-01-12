package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.PersonnelPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class PersonnelTest {
    @Autowired
    PersonnelDao personnelDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void findByName() {
        PersonnelPO personnelPO = personnelDao.findByName("chen");
        Assertions.assertEquals(Role.FINANCIAL_STAFF, personnelPO.getRole());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAll() {
        List<PersonnelPO> all = personnelDao.findAll();
        Assertions.assertEquals(12, all.size());
    }
}
