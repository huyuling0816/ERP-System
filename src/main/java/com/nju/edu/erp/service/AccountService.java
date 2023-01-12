package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    void addAccount(AccountVO accountVO);

    void deleteByName(String name);

    AccountVO updateById(AccountVO accountVO, String newName);

    /**
     * 修改账户余额
     * @param accountPO
     * @return
     */
    void updateAmount(AccountPO accountPO);

    List<AccountVO> findByKeyword(String keyword);

    List<AccountVO> getAllAccounts();

    AccountPO findAccountByName(String newName);

}
