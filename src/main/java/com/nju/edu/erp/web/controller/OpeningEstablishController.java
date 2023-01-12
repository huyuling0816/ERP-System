package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.CreateProductPO;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.CreateProductVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.OpeningEstablishService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/opening")
public class OpeningEstablishController {
    private final OpeningEstablishService openingEstablishService;
    private List<CreateProductVO> productList;
    private List<CustomerVO> customerList;
    private List<AccountVO> accountList;

    @Autowired
    public OpeningEstablishController(OpeningEstablishService openingEstablishService) {
        this.openingEstablishService = openingEstablishService;
        this.productList = new ArrayList<>();
        this.customerList = new ArrayList<>();
        this.accountList = new ArrayList<>();
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/establish-product")
    public Response establishProduct(@RequestBody CreateProductVO product) {
        this.productList.add(product);
        return Response.buildSuccess("建立商品完成");
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/establish-customer")
    public Response establishCustomer(@RequestBody CustomerVO customer) {
        this.customerList.add(customer);
        return Response.buildSuccess("建立客户完成");
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/establish-account")
    public Response establishAccount(@RequestBody AccountVO account) {
        this.accountList.add(account);
        return Response.buildSuccess("建立账户完成");
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/establish")
    public Response establish() {
        openingEstablishService.establish(productList, customerList, accountList);
        this.productList = new ArrayList<>();
        this.customerList = new ArrayList<>();
        this.accountList = new ArrayList<>();
        return Response.buildSuccess("期初建账完成");
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping(value = "/get-product")
    public Response getProduct() {
        List<CreateProductPO> allProduct = openingEstablishService.getAllProduct();
        return Response.buildSuccess(openingEstablishService.getAllProduct());
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping(value = "/get-customer")
    public Response getCustomer() {
        return Response.buildSuccess(openingEstablishService.getAllCustomer());
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @GetMapping(value = "/get-account")
    public Response getAccount() {
        return Response.buildSuccess(openingEstablishService.getAllAccount());
    }

}
