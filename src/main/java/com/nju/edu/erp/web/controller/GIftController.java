package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.service.GiftSheetService;
import com.nju.edu.erp.service.strategy.approvalStrategy.GiftSheetApproval;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/gift")
public class GIftController {
    private final GiftSheetService giftSheetService;
    private final GiftSheetApproval giftSheetApproval;

    @Autowired
    public GIftController(GiftSheetService giftSheetService, GiftSheetApproval giftSheetApproval) {
        this.giftSheetService = giftSheetService;
        this.giftSheetApproval = giftSheetApproval;
    }

    /**
     * 根据状态查看赠品单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) GiftSheetState state)  {
        return Response.buildSuccess(giftSheetService.getGiftSheetByState(state));
    }

    @GetMapping(value = "/approval")
    @Authorized (roles = {Role.GM, Role.ADMIN})
    public Response firstApproval(@RequestParam("giftSheetId") String giftSheetId,
                                  @RequestParam("state") GiftSheetState state)  {
        if(state.equals(GiftSheetState.FAILURE) || state.equals(GiftSheetState.SUCCESS)) {
            giftSheetApproval.approval(giftSheetId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
}
