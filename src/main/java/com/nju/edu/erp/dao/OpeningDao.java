package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OpeningDao {
    int createProduct(CreateProductPO createProductPO);

    int createCustomer(CreateCustomerPO createCustomerPO);

    int createAccount(CreateAccountPO createAccountPO);

    List<CreateProductPO> findAllProduct();

    List<CreateCustomerPO> findAllCustomer();

    List<CreateAccountPO> findAllAccount();
}
