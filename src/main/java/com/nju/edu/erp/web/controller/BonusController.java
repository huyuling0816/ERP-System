package com.nju.edu.erp.web.controller;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.vo.salary.BonusVO;
import com.nju.edu.erp.service.BonusService;
import com.nju.edu.erp.web.Response;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping(path = "/bonus")
public class BonusController {

    private final BonusService bonusService;

    public BonusController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @GetMapping("/searchSalary")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response searchSalary(@RequestParam Integer uid,
                                 @RequestParam Integer year,
                                 @RequestParam BigDecimal salary,
                                 @RequestParam BigDecimal bonus){
        BonusVO bonusVO = BonusVO.builder()
                .uid(uid)
                .year(year)
                .salary(salary)
                .bonus(bonus)
                .build();
        return Response.buildSuccess(bonusService.searchSalary(bonusVO));
    }

    @PostMapping("/make")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response makeBonus(@RequestBody BonusVO bonusVO){
        bonusService.makeBonus(bonusVO);
        return Response.buildSuccess();
    }

    @GetMapping("/getBonusList")
    @Authorized(roles = {Role.ADMIN,Role.GM})
    public Response getBonusList(){
        return Response.buildSuccess(bonusService.getBonusList());
    }
}
