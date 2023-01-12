package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.service.PaymentService;
import com.nju.edu.erp.service.strategy.approvalStrategy.PaymentSheetApproval;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

    private final PaymentService paymentService;

    private final PaymentSheetApproval paymentSheetApproval;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentSheetApproval paymentSheetApproval) {
        this.paymentService = paymentService;
        this.paymentSheetApproval = paymentSheetApproval;
    }

    /**
     * 财务人员制定付款单
     */
    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/make-sheet")
    public Response makePayment(UserVO userVO, @RequestBody PaymentSheetVO paymentSheetVO) {
        paymentService.makePaymentSheet(userVO, paymentSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看付款单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) FinanceSheetState state) {
        return Response.buildSuccess(paymentService.getPaymentSheetByState(state));
    }

    /**
     * 总经理审批
     *
     * @param paymentId 进货单id
     * @param state     修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/approval")
    public Response Approval(@RequestParam("paymentSheetId") String paymentId,
                             @RequestParam("state") FinanceSheetState state) {
        if (state.equals(FinanceSheetState.FAILURE) || state.equals(FinanceSheetState.SUCCESS)) {
            paymentSheetApproval.approval(paymentId, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败");
        }
    }
}
