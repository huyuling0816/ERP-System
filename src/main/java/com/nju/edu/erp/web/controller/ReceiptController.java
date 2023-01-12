package com.nju.edu.erp.web.controller;
import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.ReceiptService;
import com.nju.edu.erp.service.strategy.approvalStrategy.ReceiptSheetApproval;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    private final ReceiptSheetApproval receiptSheetApproval;

    @Autowired
    public ReceiptController(ReceiptService receiptService, ReceiptSheetApproval receiptSheetApproval){this.receiptService = receiptService;
        this.receiptSheetApproval = receiptSheetApproval;
    }

    /**
     * 财务人员制定收款单
     */
    @Authorized (roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/make-sheet")
    public Response makeReceipt(UserVO userVO, @RequestBody ReceiptSheetVO receiptSheetVO)  {
        receiptService.makeReceiptSheet(userVO,receiptSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看收款单
     */
    @GetMapping(value = "/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false)FinanceSheetState state)  {
        return Response.buildSuccess(receiptService.getReceiptSheetByState(state));
    }

    /**
     * 总经理审批
     * @param receiptId 进货单id
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized (roles = {Role.GM, Role.ADMIN})
    @GetMapping(value = "/approval")
    public Response Approval(@RequestParam("receiptSheetId") String receiptId,
                             @RequestParam("state") FinanceSheetState state)  {
        if(state.equals(FinanceSheetState.FAILURE) || state.equals(FinanceSheetState.SUCCESS)) {
            receiptSheetApproval.approval(receiptId,state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000","操作失败"); // code可能得改一个其他的
        }
    }
}
