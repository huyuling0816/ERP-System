package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.CreateAccountPO;
import com.nju.edu.erp.model.po.CreateCustomerPO;
import com.nju.edu.erp.model.po.CreateProductPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.CreateProductVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpeningEstablishService {

    void establish(List<CreateProductVO> createProductVOList,
                          List<CustomerVO> customerVOList,
                          List<AccountVO> accountVOList);

    List<CreateProductPO> getAllProduct();

    List<CreateCustomerPO> getAllCustomer();

    List<CreateAccountPO> getAllAccount();

}
