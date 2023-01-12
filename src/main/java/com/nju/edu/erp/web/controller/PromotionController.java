package com.nju.edu.erp.web.controller;
import java.util.ArrayList;
import java.util.List;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByPricePacksVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByTotalPriceVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByUserLevelVO;
import com.nju.edu.erp.service.PromotionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "promotion-strategy")
public class PromotionController {

    private final PromotionService promotionService;
    private List<PromotionStrategyByUserLevelVO>  promotionStrategyByUserLevelVOS;
    private List<PromotionStrategyByTotalPriceVO> promotionStrategyByTotalPriceVOS;
    private List<PromotionStrategyByPricePacksVO> promotionStrategyByPricePacksVOS;

    @Autowired
    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
        this.promotionStrategyByUserLevelVOS = new ArrayList<>();
        this.promotionStrategyByTotalPriceVOS = new ArrayList<>();
        this.promotionStrategyByPricePacksVOS = new ArrayList<>();
    }

    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/get-all-strategy-pack")
    public Response getAllStrategyByPricePack() {
        List<PromotionStrategyByPricePacksVO> allPromotionStrategyByPricePacks = promotionService.findAllPromotionStrategyByPricePacks();
        return Response.buildSuccess(allPromotionStrategyByPricePacks);
    }

    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/get-all-strategy-total")
    public Response getAllStrategyByTotalPrice() {
        List<PromotionStrategyByTotalPriceVO> allPromotionStrategyByTotalPrice = promotionService.findAllPromotionStrategyByTotalPrice();
        return Response.buildSuccess(allPromotionStrategyByTotalPrice);
    }

    @Authorized(roles = {Role.GM, Role.ADMIN})
    @PostMapping(value = "/add-user-level")
    public Response addUserLevel(@RequestBody PromotionStrategyByUserLevelVO vo){
        this.promotionStrategyByUserLevelVOS.add(vo);
        System.out.println(this.promotionStrategyByTotalPriceVOS);
        return Response.buildSuccess("成功增加一条客户级别促销策略");
    }

    @Authorized(roles = {Role.ADMIN,Role.GM})
    @PostMapping(value = "/add-total-price")
    public Response addTotalPrice(@RequestBody PromotionStrategyByTotalPriceVO vo){
        this.promotionStrategyByTotalPriceVOS.add(vo);
        return Response.buildSuccess("成功增加一条总价促销策略");
    }

    @Authorized(roles = {Role.ADMIN,Role.GM})
    @PostMapping(value = "/add-price-pack")
    public Response addTotalPrice(@RequestBody PromotionStrategyByPricePacksVO vo){
        this.promotionStrategyByPricePacksVOS.add(vo);
        return Response.buildSuccess("成功增加一条特价包策略");
    }

    @Authorized(roles = {Role.ADMIN, Role.GM})
    @PostMapping(value = "/make-by-user-level")
    public Response makePromotionStrategyByUserLevel(){
        promotionService.makePromotionStrategyByUserLevel(promotionStrategyByUserLevelVOS);
        this.promotionStrategyByUserLevelVOS = new ArrayList<>();
        return Response.buildSuccess("制定用户级别促销策略成功");
    }

    @Authorized(roles = {Role.ADMIN, Role.GM})
    @PostMapping(value = "/make-by-price-packs")
    public Response makePromotionStrategyByPricePacks(){
        this.promotionStrategyByPricePacksVOS.forEach(promotionStrategyByPricePacksVO -> {
            this.promotionService.makePromotionStrategyByPricePacks(promotionStrategyByPricePacksVO);
        });
        this.promotionStrategyByPricePacksVOS = new ArrayList<>();
        return Response.buildSuccess("制定特价包促销策略成功");
    }

    @Authorized( roles = {Role.ADMIN, Role.GM})
    @PostMapping(value = "/make-by-total-price")
    public Response makePromotionStrategyByTotalPrice(){
        this.promotionService.makePromotionStrategyByTotalPrice(promotionStrategyByTotalPriceVOS);
        this.promotionStrategyByTotalPriceVOS = new ArrayList<>();
        return Response.buildSuccess("制定总价促销策略成功");
    }
    @Authorized( roles = {Role.ADMIN, Role.GM})
    @GetMapping(value = "/get-all-user-level-strategy")
    public Response getAllPromotionStrategyByUserLevel(){
        return Response.buildSuccess(promotionService.findAllPromotionStrategyByUserLevel());
    }
}
