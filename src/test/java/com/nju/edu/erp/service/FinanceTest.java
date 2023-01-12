package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.strategy.approvalStrategy.SalarySheetApproval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class FinanceTest {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountDao accountDao;
    @Autowired
    SalaryService salaryService;
    @Autowired
    SalarySheetDao salarySheetDao;
    @Autowired
    SalarySheetApproval salarySheetApproval;

    @Test
    @Transactional
    @Rollback(value = true)
    public void addAccount(){ // 测试账户是否插入、删除成功
        AccountVO accountVO = AccountVO.builder()
                .name("中国农业银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        accountService.addAccount(accountVO);
        AccountPO latestAccount = accountDao.getLatestAccount();
        Assertions.assertNotNull(latestAccount);
        Assertions.assertEquals(0,latestAccount.getName().compareTo("中国农业银行"));
        Assertions.assertEquals(0,latestAccount.getAmount().compareTo(BigDecimal.valueOf(100000)));
        accountService.deleteByName("中国农业银行");
        Assertions.assertNull(accountDao.findByName("中国农业银行"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void updateName(){ // 测试账户名称是否更新成功
        AccountVO accountVO = AccountVO.builder()
                .name("中国农业银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        accountService.addAccount(accountVO);
        AccountVO res = accountService.updateById(accountVO,"中国工商银行");
        Assertions.assertNotNull(res);
        Assertions.assertEquals(0,res.getName().compareTo("中国工商银行"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void findAccountByKeyword(){ // 测试是否能模糊查询
        AccountVO accountVO1 = AccountVO.builder()
                .name("中国农业银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        AccountVO accountVO2 = AccountVO.builder()
                .name("江苏银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        AccountVO accountVO3 = AccountVO.builder()
                .name("中国工商银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        accountService.addAccount(accountVO1);
        accountService.addAccount(accountVO2);
        accountService.addAccount(accountVO3);
        List<AccountVO> res = accountService.findByKeyword("中国");
        Assertions.assertEquals(2,res.size());
        Assertions.assertEquals(0,res.get(0).getName().compareTo("中国农业银行"));
        Assertions.assertEquals(0,res.get(1).getName().compareTo("中国工商银行"));
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void generateSalarySheet(){ // 测试是否正确生成工资单
        SalarySheetVO salarySheetVO = SalarySheetVO.builder()
                .uname("manager")
                .accountName("123456")
                .rawSalary(BigDecimal.valueOf(10000))
                .housingProvidentFund(BigDecimal.valueOf(1000))
                .personalIncomeTax(BigDecimal.valueOf(1000))
                .unemploymentInsurance(BigDecimal.valueOf(1000))
                .build();
        salaryService.makeSalarySheet(salarySheetVO);
        SalarySheetPO latest = salarySheetDao.getLatestSheet();
        Assertions.assertNotNull(latest);
        Assertions.assertEquals(0,latest.getActualSalary().compareTo(BigDecimal.valueOf(7000)));
        Assertions.assertEquals(0,latest.getId().compareTo("GZD-20220701-00000"));    //日期要为当天
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_1(){ // 测试工资单审批后，修改账户余额
        SalarySheetVO salarySheetVO = SalarySheetVO.builder()
                .uname("manager")
                .accountName("中国农业银行")
                .rawSalary(BigDecimal.valueOf(10000))
                .housingProvidentFund(BigDecimal.valueOf(1000))
                .personalIncomeTax(BigDecimal.valueOf(1000))
                .unemploymentInsurance(BigDecimal.valueOf(1000))
                .build();
        salaryService.makeSalarySheet(salarySheetVO);
        AccountVO accountVO = AccountVO.builder()
                .name("中国农业银行")
                .amount(BigDecimal.valueOf(100000))
                .build();
        accountService.addAccount(accountVO);
        Assertions.assertEquals(SalarySheetState.PENDING,salarySheetDao.getLatestSheet().getState());
        salarySheetApproval.approval("GZD-20220701-00000",SalarySheetState.SUCCESS);
        SalarySheetPO salarySheetPO = salarySheetDao.getSheetById("GZD-20220701-00000");
        Assertions.assertEquals(SalarySheetState.SUCCESS,salarySheetPO.getState());
        Assertions.assertEquals(0,accountDao.getLatestAccount().getAmount().compareTo(BigDecimal.valueOf(107000)));
    }
}
