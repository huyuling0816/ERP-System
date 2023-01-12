package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PersonnelDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.PersonnelPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.service.PersonnelService;
import com.nju.edu.erp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonnelServiceImpl implements PersonnelService {
    private final PersonnelDao personnelDao;
    private final UserService userService;

    @Autowired
    public PersonnelServiceImpl(PersonnelDao personnelDao, UserService userService){
        this.personnelDao=personnelDao;
        this.userService=userService;
    }

    @Transactional
    @Override
    public PersonnelVO createPersonnel(PersonnelVO personnelVO){
        if(personnelVO.getPhoneNumber().length()!=11){
            throw new MyServiceException("Personnel0004","电话号码应该为11位，请修改！");
        }
        PersonnelPO personnelPO=personnelDao.findByName(personnelVO.getName());
        if(personnelPO!=null){
            throw new MyServiceException("Personnel0003","该员工已存在");
        }
        personnelPO=new PersonnelPO();
        BeanUtils.copyProperties(personnelVO,personnelPO);
        personnelDao.createPersonnel(personnelPO);
        UserVO userVO=new UserVO();     //系统自动创建账号，用户名为手机号，初始密码123456（应该有第一次登录修改密码的需求）
        userVO.setName(personnelVO.getName());
        userVO.setPassword("123456");
        userVO.setRole(personnelVO.getRole());
        userService.register(userVO);
        PersonnelPO temp=personnelDao.findByName(personnelVO.getName());
        if(temp==null){
            throw new MyServiceException("Personnel0001","创建失败");
        }
        PersonnelVO res=new PersonnelVO();
        BeanUtils.copyProperties(temp,res);
        return res;     //返回创建的员工
    }

    @Transactional
    @Override
    public PersonnelVO findById(Integer id){
        PersonnelPO personnelPO=personnelDao.findById(id);
        if(personnelPO==null){
            throw new MyServiceException("Personnel0002","该员工不存在");
        }
        PersonnelVO personnelVO=new PersonnelVO();
        BeanUtils.copyProperties(personnelPO,personnelVO);
        return personnelVO;
    }

    @Transactional
    @Override
    public PersonnelVO findByName(String name){
        PersonnelPO personnelPO=personnelDao.findByName(name);
        if(personnelPO==null){
            throw new MyServiceException("Personnel0002","该员工不存在");
        }
        PersonnelVO personnelVO=new PersonnelVO();
        BeanUtils.copyProperties(personnelPO,personnelVO);
        return personnelVO;
    }

    @Transactional
    @Override
    public List<PersonnelVO> findAllPersonnel(){
        List<PersonnelPO> personnelPOS = personnelDao.findAll();
        List<PersonnelVO> personnelVOS=new ArrayList<>();
        for (PersonnelPO personnelPO:personnelPOS){
            PersonnelVO temp=new PersonnelVO();
            BeanUtils.copyProperties(personnelPO,temp);
            personnelVOS.add(temp);
        }
        return personnelVOS;
    }

    @Transactional
    @Override
    public PersonnelVO updatePersonnel(PersonnelVO personnelVO){
        if(personnelVO.getPhoneNumber().length()!=11){
            throw new MyServiceException("Personnel0004","电话号码应该为11位，请修改！");
        }
        PersonnelPO personnelPO=new PersonnelPO();
        BeanUtils.copyProperties(personnelVO,personnelPO);
        int num=personnelDao.updateByName(personnelPO);
        if(num==0){
            throw new MyServiceException("Personnel0005","更新失败");
        }
        PersonnelPO temp=personnelDao.findById(personnelVO.getId());
        PersonnelVO res=new PersonnelVO();
        BeanUtils.copyProperties(temp,res);
        return res;
    }

    @Transactional
    @Override
    public void deleteById(Integer id){
        PersonnelPO personnelPO=personnelDao.findById(id);
        if(personnelPO==null){
            throw new MyServiceException("Personnel0002","该员工不存在");
        }
        personnelDao.deleteById(id);
    }
}