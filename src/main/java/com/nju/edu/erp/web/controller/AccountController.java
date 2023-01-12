package com.nju.edu.erp.web.controller;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/findAll")
    public Response findAllAccounts(){
        return Response.buildSuccess(accountService.getAllAccounts());
    }

    @Authorized (roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/add")
    public Response addAccount(@RequestBody AccountVO accountVO){
        accountService.addAccount(accountVO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response deleteAccount(@RequestParam String bankAccount){
        accountService.deleteByName(bankAccount);
        return Response.buildSuccess();
    }

    @GetMapping ("/update")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response updateAmount(@RequestParam (value = "bankAccount") String bankAccount,
                                 @RequestParam (value = "transferAmount") String transferAmount) {
        AccountPO po = accountService.findAccountByName(bankAccount);
        double d = Double.parseDouble(transferAmount);
        po.setAmount(BigDecimal.valueOf(d));
        accountService.updateAmount(po);
        return Response.buildSuccess();
    }

    @GetMapping("/query")
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    public Response findByKeyword(@RequestParam String keyword) {
        return Response.buildSuccess(accountService.findByKeyword(keyword));
    }

}
