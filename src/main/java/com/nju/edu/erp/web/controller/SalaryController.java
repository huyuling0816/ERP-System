package com.nju.edu.erp.web.controller;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.service.strategy.approvalStrategy.ApprovalStrategy;
import com.nju.edu.erp.service.strategy.approvalStrategy.SalarySheetApproval;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {
    private final SalaryService salaryService;
    private final ApprovalStrategy approvalStrategy;

    @Autowired
    public SalaryController(SalaryService salaryService, SalarySheetApproval approvalStrategy){
        this.salaryService=salaryService;
        this.approvalStrategy=approvalStrategy;
    }

    @PostMapping("/make")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response makeSalarySheet(@RequestBody SalarySheetVO salarySheetVO){
        salaryService.makeSalarySheet(salarySheetVO);
        return Response.buildSuccess();
    }

    @GetMapping("/makeSimple")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response makeSimpleSalarySheet(@RequestParam Integer uid){
        return Response.buildSuccess(salaryService.makeSimpleSalarySheet(uid));
    }

    @GetMapping("/makeAll")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response makeAllSalarySheet(){
        salaryService.makeAllSalarySheet();
        return Response.buildSuccess();
    }

    @GetMapping("/getByState")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response getByState(@RequestParam SalarySheetState state){
        return Response.buildSuccess(salaryService.getSalaryServiceByState(state));
    }


    @GetMapping("/approve")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response approval(@RequestParam String salarySheetId,@RequestParam SalarySheetState state){
        approvalStrategy.approval(salarySheetId,state);
        return Response.buildSuccess();
    }

    @GetMapping("/getAll")
    @Authorized(roles = {Role.ADMIN, Role.HR,Role.GM})
    public Response getAllSalarySheet(){
        return Response.buildSuccess(salaryService.getAllSalarySheet());
    }
    
}
