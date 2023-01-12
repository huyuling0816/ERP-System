package com.nju.edu.erp.web.controller;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.attend.AttendanceVO;
import com.nju.edu.erp.service.AttendanceService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/attend")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService=attendanceService;
    }


    @Authorized(roles = {Role.FINANCIAL_STAFF,Role.INVENTORY_MANAGER,Role.SALE_STAFF,Role.SALE_MANAGER,
            Role.HR,Role.ADMIN})
    @PostMapping(value = "/add")
    public Response addAttendRecord(@RequestBody AttendanceVO attendanceVO){
        attendanceService.addAttendRecord(attendanceVO);
        return Response.buildSuccess();
    }

    @Authorized(roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/delete")
    public Response deletePreRecord(@RequestParam String dateStr){
        attendanceService.deletePreRecord(dateStr);
        return Response.buildSuccess();
    }

    @Authorized(roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/searchMonth")
    public Response countMonthAttend(@RequestParam Integer uid){
        return Response.buildSuccess(attendanceService.countMonthRecord(uid));
    }

    @Authorized(roles = {Role.HR,Role.FINANCIAL_STAFF,Role.INVENTORY_MANAGER,Role.SALE_MANAGER,
            Role.SALE_STAFF,Role.ADMIN})
    @GetMapping(value = "/searchDay")
    public Response countDayAttend(@RequestParam Integer uid){
        return Response.buildSuccess(attendanceService.countDayAttend(uid));
    }

    @Authorized(roles = {Role.HR, Role.ADMIN})
    @GetMapping(value = "/findAll")
    public Response findAllAttendRecord(){
        return Response.buildSuccess(attendanceService.findAll());
    }
}
