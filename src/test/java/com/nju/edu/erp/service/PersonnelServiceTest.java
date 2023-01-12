package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.PersonnelDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.PersonnelPO;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PersonnelServiceTest {
    @Autowired
    PersonnelService personnelService;

    @Autowired
    PersonnelDao personnelDao;


    @Test
    @Transactional
    @Rollback(value = true)
    public void createPersonnel(){
        PersonnelVO personnelVO=PersonnelVO.builder()
                        .name("张三")
                        .gender('M')
                        .birthday("2002-01-09")
                        .role(Role.INVENTORY_MANAGER)
                        .phoneNumber("12345678901")
                        .cardNumber("1234567890123456789")
                        .build();
        personnelService.createPersonnel(personnelVO);
        PersonnelPO personnelPO=personnelDao.findByName("张三");
        Assertions.assertNotNull(personnelPO);
        Assertions.assertEquals('M',personnelPO.getGender());
        Assertions.assertEquals(Role.INVENTORY_MANAGER,personnelPO.getRole());
        Assertions.assertEquals("12345678901",personnelPO.getPhoneNumber());
    }
}
