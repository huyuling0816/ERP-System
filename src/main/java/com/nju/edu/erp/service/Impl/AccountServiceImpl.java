package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    public AccountServiceImpl(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @Override
    public void addAccount(AccountVO accountVO) {
        AccountPO accountPO = accountDao.findByName(accountVO.getName());
        if(accountPO != null){
            throw new MyServiceException("A0001", "账号名已经存在");
        }
        AccountPO accountPOSave = new AccountPO();
        BeanUtils.copyProperties(accountVO,accountPOSave);
        accountDao.addAccount(accountPOSave);
    }

    @Override
    public void deleteByName(String name) {
        AccountPO accountPO = accountDao.findByName(name);
        if(accountPO == null){
            throw new MyServiceException("B0004", "删除失败！");
        }
        int ans = accountDao.deleteByName(name);
        if(ans == 0){
            throw new MyServiceException("B0004", "删除失败！");
        }
    }

    @Override
    public AccountVO updateById(AccountVO accountVO, String newName) {
        int id = accountDao.findByName(accountVO.getName()).getId();
        AccountPO accountPO = accountDao.findById(id);
        accountPO.setName(newName);
        int ans = accountDao.updateOne(accountPO);
        if(ans == 0){
            throw new MyServiceException("A0003","修改失败！");
        }
        AccountPO queryAns = accountDao.findById(id);
        AccountVO res = new AccountVO();
        BeanUtils.copyProperties(queryAns,res);
        return res;
    }

    @Override
    public void updateAmount(AccountPO accountPO) {
        accountDao.updateOne(accountPO);
    }

    @Override
    public List<AccountVO> findByKeyword(String keyword) {
        List<AccountPO> accountPOS = accountDao.findByKeyword(keyword);
        if(accountPOS == null) return null;
        List<AccountVO> accountVOS = new ArrayList<>();
        for(AccountPO accountPO:accountPOS){
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(accountPO,accountVO);
            accountVOS.add(accountVO);
        }
        return accountVOS;
    }
    @Override
    public List<AccountVO> getAllAccounts() {
        List<AccountPO> accountPOS = accountDao.findAll();
        if(accountPOS == null) return null;
        List<AccountVO> res = new ArrayList<>();
        for(AccountPO accountPO : accountPOS) {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(accountPO,accountVO);
            res.add(accountVO);
        }
        return res;
    }
    @Override
    public AccountPO findAccountByName(String newName) {
        return accountDao.findByName(newName);
    }

}
