package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalarySystemService {
    SalarySystemVO insertSalarySystem(SalarySystemVO salarySystemVO);

    SalarySystemVO updateByRole(SalarySystemVO salarySystemVO);

    void deleteByRole(Role role);

    SalarySystemVO findSalarySystem(Role role);

    List<SalarySystemVO> findAll();
}
