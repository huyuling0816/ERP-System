package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    int updateOne(CustomerPO customerPO);

    CustomerPO findOneById(Integer supplier);

    CustomerPO findOneByName(String name);

    List<CustomerPO> findAll();

    List<CustomerPO> findAllByType(CustomerType customerType);

    int addCustomer(CustomerPO customerPO);

    int deleteById(int id);
}
