package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.OpeningDao;
import com.nju.edu.erp.model.po.CreateAccountPO;
import com.nju.edu.erp.model.po.CreateCustomerPO;
import com.nju.edu.erp.model.po.CreateProductPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.CreateProductVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.OpeningEstablishService;
import com.nju.edu.erp.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpeningEstablishServiceImpl implements OpeningEstablishService {

    ProductService productService;
    CustomerService customerService;
    AccountService accountService;
    OpeningDao openingDao;

    @Autowired
    public OpeningEstablishServiceImpl(ProductService productService, CustomerService customerService, AccountService accountService, OpeningDao openingDao) {
        this.productService = productService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.openingDao = openingDao;
    }


    @Override
    public void establish(List<CreateProductVO> createProductVOList,
                          List<CustomerVO> customerVOList,
                          List<AccountVO> accountVOList) {
        if(createProductVOList.size() != 0) {
            createProductVOList.forEach(createProductVO -> {
                productService.createProduct(createProductVO);
                CreateProductPO createProductPO = new CreateProductPO();
                BeanUtils.copyProperties(createProductVO, createProductPO);
                openingDao.createProduct(createProductPO);
            });
        }
        if(customerVOList.size() != 0) {
            customerVOList.forEach(customerVO -> {
                customerService.addCustomer(customerVO);
                CreateCustomerPO createCustomerPO = new CreateCustomerPO();
                BeanUtils.copyProperties(customerVO, createCustomerPO);
                openingDao.createCustomer(createCustomerPO);
            });
        }
        if(accountVOList.size() != 0) {
            accountVOList.forEach(accountVO -> {
                accountService.addAccount(accountVO);
                CreateAccountPO createAccountPO = new CreateAccountPO();
                BeanUtils.copyProperties(accountVO, createAccountPO);
                openingDao.createAccount(createAccountPO);
            });
        }
    }

    @Override
    public List<CreateProductPO> getAllProduct() {
        return openingDao.findAllProduct();
    }

    @Override
    public List<CreateCustomerPO> getAllCustomer() {
        return openingDao.findAllCustomer();
    }

    @Override
    public List<CreateAccountPO> getAllAccount() {
        return openingDao.findAllAccount();
    }
}
