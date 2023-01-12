package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.SalarySystemDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.SalarySystemPO;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import com.nju.edu.erp.service.SalarySystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalarySystemServiceImpl implements SalarySystemService {
    private final SalarySystemDao salarySystemDao;

    @Autowired
    public SalarySystemServiceImpl(SalarySystemDao salarySystemDao){
        this.salarySystemDao=salarySystemDao;
    }

    @Override
    @Transactional
    public SalarySystemVO insertSalarySystem(SalarySystemVO salarySystemVO){
        if(salarySystemDao.findByRole(salarySystemVO.getRole())!=null){
            throw new MyServiceException("SalarySystem0001","重复制定");
        }
        SalarySystemPO salarySystemPO=new SalarySystemPO();
        BeanUtils.copyProperties(salarySystemVO,salarySystemPO);
        salarySystemDao.insertSalarySystem(salarySystemPO);
        SalarySystemPO temp=salarySystemDao.findByRole(salarySystemVO.getRole());
        if(temp==null){
            throw new MyServiceException("SalarySystem0002","创建失败");
        }
        SalarySystemVO res=new SalarySystemVO();
        BeanUtils.copyProperties(temp,res);
        return res;
    }

    @Override
    @Transactional
    public SalarySystemVO updateByRole(SalarySystemVO salarySystemVO){
        SalarySystemPO salarySystemPO=new SalarySystemPO();
        BeanUtils.copyProperties(salarySystemVO,salarySystemPO);
        int ans=salarySystemDao.updateByRole(salarySystemPO);
        if(ans==0){
            throw new MyServiceException("SalarySystem0003","更新失败");
        }
        SalarySystemPO temp=salarySystemDao.findByRole(salarySystemVO.getRole());
        SalarySystemVO res=new SalarySystemVO();
        BeanUtils.copyProperties(temp,res);
        return res;
    }

    @Override
    @Transactional
    public void deleteByRole(Role role){
        int ans=salarySystemDao.deleteByRole(role);
        if(ans==0){
            throw new MyServiceException("SalarySystem0004","删除失败");
        }
    }


    @Override
    @Transactional
    public SalarySystemVO findSalarySystem(Role role){
        SalarySystemPO salarySystemPO=salarySystemDao.findByRole(role);
        if(salarySystemPO==null){
            throw new RuntimeException("没有对应的薪酬规则");
        }
        SalarySystemVO salarySystemVO=new SalarySystemVO();
        BeanUtils.copyProperties(salarySystemPO,salarySystemVO);
        return salarySystemVO;
    }

    @Override
    @Transactional
    public List<SalarySystemVO> findAll(){
        List<SalarySystemVO> res=new ArrayList<>();
        List<SalarySystemPO> salarySystemPOS=salarySystemDao.findAll();
        for(SalarySystemPO salarySystemPO:salarySystemPOS){
            SalarySystemVO salarySystemVO=new SalarySystemVO();
            BeanUtils.copyProperties(salarySystemPO,salarySystemVO);
            res.add(salarySystemVO);
        }
        return res;
    }
}
