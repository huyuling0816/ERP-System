package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @GetMapping("/findAll")
    public Response findAll() {
        return Response.buildSuccess(customerService.getAllCustomers());
    }


    @PostMapping(value = "/add")
    @Authorized(roles = {Role.ADMIN,Role.SALE_STAFF,Role.GM})
    public Response addCustomer(@RequestBody CustomerVO customerVO){
        customerService.addCustomer(customerVO);
        return Response.buildSuccess();
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN,Role.SALE_STAFF,Role.GM})
    public Response deleteCustomer(@RequestParam int id){
        customerService.deleteById(id);
        return Response.buildSuccess();
    }


}
