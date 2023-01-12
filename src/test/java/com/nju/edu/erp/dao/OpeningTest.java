package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.CreateProductPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OpeningTest {
    @Autowired
    OpeningDao openingDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void create() {
        CreateProductPO createProductPO1 = CreateProductPO.builder()
                .categoryId(1)
                .name("product1")
                .type("type1")
                .purchasePrice(BigDecimal.valueOf(100))
                .retailPrice(BigDecimal.valueOf(200))
                .build();
        CreateProductPO createProductPO2 = CreateProductPO.builder()
                .categoryId(1)
                .name("product2")
                .type("type2")
                .purchasePrice(BigDecimal.valueOf(100))
                .retailPrice(BigDecimal.valueOf(200))
                .build();

        int res = openingDao.createProduct(createProductPO1);
        int res1 = openingDao.createProduct(createProductPO2);

        Assertions.assertEquals(1, res);
        Assertions.assertEquals(1, res1);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAll() {
        CreateProductPO createProductPO1 = CreateProductPO.builder()
                .categoryId(1)
                .name("product1")
                .type("type1")
                .purchasePrice(BigDecimal.valueOf(100))
                .retailPrice(BigDecimal.valueOf(200))
                .build();
        CreateProductPO createProductPO2 = CreateProductPO.builder()
                .categoryId(1)
                .name("product2")
                .type("type2")
                .purchasePrice(BigDecimal.valueOf(100))
                .retailPrice(BigDecimal.valueOf(200))
                .build();

        openingDao.createProduct(createProductPO1);
        openingDao.createProduct(createProductPO2);

        List<CreateProductPO> allProduct = openingDao.findAllProduct();
        Assertions.assertEquals(2, allProduct.size());
    }
}
