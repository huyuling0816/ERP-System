package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.SalarySystemPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalarySystemDao {
    int insertSalarySystem(SalarySystemPO salarySystemPO);

    int updateByRole(SalarySystemPO salarySystemPO);

    SalarySystemPO findByRole(Role role);


    List<SalarySystemPO> findAll();

    int deleteByRole(Role role);
}
