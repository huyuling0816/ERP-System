package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import com.nju.edu.erp.service.SalarySystemService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/salarySystem")
public class SalarySystemController {
    private final SalarySystemService salarySystemService;

    @Autowired
    public SalarySystemController(SalarySystemService salarySystemService){
        this.salarySystemService=salarySystemService;
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response createSalarySystem(@RequestBody SalarySystemVO salarySystemVO){
        return Response.buildSuccess(salarySystemService.insertSalarySystem(salarySystemVO));
    }

    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response updatePersonnel(@RequestBody SalarySystemVO salarySystemVO){
        return Response.buildSuccess(salarySystemService.updateByRole(salarySystemVO));
    }

    @GetMapping("/findAll")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response findAllPersonnel(){
        return Response.buildSuccess(salarySystemService.findAll());
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response deletePersonnel(@RequestParam Role role){
        salarySystemService.deleteByRole(role);
        return Response.buildSuccess();
    }
}
