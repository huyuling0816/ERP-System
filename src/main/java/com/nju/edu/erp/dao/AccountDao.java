package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface AccountDao {

    int addAccount(AccountPO accountPO);

    int deleteByName(String name);

    /**
     * 修改账户信息
     * @param accountPO
     * @return
     */
    int updateOne(AccountPO accountPO);

    AccountPO findByName(String name);

    AccountPO findById(Integer id);

    AccountPO getLatestAccount();

    // 通过关键字进行模糊查找
    List<AccountPO> findByKeyword(String keyword);

    List<AccountPO> findAll();
}
