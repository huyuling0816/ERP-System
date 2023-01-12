package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.service.PersonnelService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/personnel")
public class PersonnelController {
    private final PersonnelService personnelService;

    @Autowired
    public PersonnelController(PersonnelService personnelService){
        this.personnelService=personnelService;
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response createPersonnel(@RequestBody PersonnelVO personnelVO){
        return Response.buildSuccess(personnelService.createPersonnel(personnelVO));
    }

    @GetMapping("/findAll")
    @Authorized(roles = {Role.ADMIN, Role.HR, Role.GM})
    public Response findAllPersonnel(){
        return Response.buildSuccess(personnelService.findAllPersonnel());
    }

    @GetMapping("/findById")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response findPersonnelById(@RequestParam Integer id){
        return Response.buildSuccess(personnelService.findById(id));
    }

    @GetMapping("/findByName")
    @Authorized(roles = {Role.ADMIN, Role.HR, Role.GM, Role.FINANCIAL_STAFF, Role.INVENTORY_MANAGER,
    Role.SALE_MANAGER,Role.SALE_STAFF})
    public Response findPersonnelByName(@RequestParam String name){
        return Response.buildSuccess(personnelService.findByName(name));
    }


    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response updatePersonnel(@RequestBody PersonnelVO personnelVO){
        return Response.buildSuccess(personnelService.updatePersonnel(personnelVO));
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.HR})
    public Response deletePersonnel(@RequestParam Integer id){
        personnelService.deleteById(id);
        return Response.buildSuccess();
    }
}
