package com.nju.edu.erp.web.controller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.SaleDetailPO;
import com.nju.edu.erp.model.vo.operationSheet.Expenditure;
import com.nju.edu.erp.model.vo.operationSheet.IncomeAfterDiscount;
import com.nju.edu.erp.model.vo.OperationSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.service.SheetsReviewService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/sheets-review")
public class SheetsReviewController {
    private final SheetsReviewService sheetsReviewService;

    @Autowired
    public SheetsReviewController(SheetsReviewService sheetsReviewService) {
        this.sheetsReviewService = sheetsReviewService;
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @GetMapping(value = "/showSaleDetail")
    public Response getSaleDetail(@RequestParam("beginDateStr") String beginDateStr,
                                  @RequestParam("endDateStr") String endDateStr,
                                  @RequestParam("productName") String productName,
                                  @RequestParam("customerName") String customerName,
                                  @RequestParam("operatorName") String operatorName,
                                  @RequestParam("warehouseId") String warehouseId) throws ParseException {
        List<SaleDetailPO> saleDetailByTime = sheetsReviewService.getSaleDetailByTime(beginDateStr, endDateStr);
        List<SaleDetailPO> saleDetailByProduct = sheetsReviewService.getSaleDetailByProduct(productName);
        List<SaleDetailPO> saleDetailByCustomer = sheetsReviewService.getSaleDetailByCustomer(customerName);
        List<SaleDetailPO> saleDetailByOperator = sheetsReviewService.getSaleDetailByOperator(operatorName);
        List<SaleDetailPO> saleDetailByWarehouse = sheetsReviewService.getSaleDetailByWarehouse(warehouseId);
        List<SaleDetailPO> res = saleDetailByTime;
        if (saleDetailByProduct != null) {
            res = res.stream().filter(saleDetailByProduct::contains).collect(Collectors.toList());
        }
        if (saleDetailByCustomer != null) {
            res = res.stream().filter(saleDetailByCustomer::contains).collect(Collectors.toList());
        }
        if (saleDetailByOperator != null) {
            res = res.stream().filter(saleDetailByOperator::contains).collect(Collectors.toList());
        }
        if (saleDetailByWarehouse != null) {
            res = res.stream().filter(saleDetailByWarehouse::contains).collect(Collectors.toList());
        }
        return Response.buildSuccess(res);
    }

    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    @GetMapping(value = "/showProcessSheet")
    public Response getProcessSheet(@RequestParam("beginDateStr") String beginDateStr,
                                    @RequestParam("endDateStr") String endDateStr,
                                    @RequestParam("sheetType") String sheetType,
                                    @RequestParam("customerName") String customerName,
                                    @RequestParam("operatorName") String operatorName,
                                    @RequestParam("warehouseId") String warehouseId) throws ParseException {
        List<SheetVO> processSheetByTime = sheetsReviewService.getSheetByTime(beginDateStr,endDateStr);
        List<SheetVO> processSheetBySheetType = sheetsReviewService.getSheetBySheetType(sheetType);
        List<SheetVO> processSheetByCustomer = sheetsReviewService.getSheetByCustomerName(customerName);
        List<SheetVO> processSheetByOperatorName = sheetsReviewService.getSheetByOperatorName(operatorName);
        List<SheetVO> res = processSheetByTime;
        if (processSheetBySheetType != null) {
            res = res.stream().filter(processSheetBySheetType::contains).collect(Collectors.toList());
        }
        if(processSheetByCustomer != null){
            res =res.stream().filter(processSheetByCustomer::contains).collect(Collectors.toList());
        }
        if(processSheetByOperatorName != null){
            res = res.stream().filter(processSheetByOperatorName::contains).collect(Collectors.toList());
        }
        return Response.buildSuccess(res);
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @GetMapping(value = "/showOperationSheet")
    public Response getOperationSheet() {
        List<OperationSheetVO> operationSheetVOList = new ArrayList<>();
        operationSheetVOList.add(sheetsReviewService.getOperationSheet());
        return Response.buildSuccess(operationSheetVOList);
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @GetMapping(value = "/showIncomeAfterDiscount")
    public Response getIncomeAfterDiscount() {
        IncomeAfterDiscount incomeAfterDiscount = sheetsReviewService.getIncomeAfterDiscount();
        List<IncomeAfterDiscount> incomeAfterDiscountList = new ArrayList<>();
        incomeAfterDiscountList.add(incomeAfterDiscount);
        return Response.buildSuccess(incomeAfterDiscountList);
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.GM, Role.ADMIN})
    @GetMapping(value = "/showExpenditure")
    public Response getExpenditure() {
        Expenditure totalExpenditure = sheetsReviewService.getTotalExpenditure();
        List<Expenditure> expenditureList = new ArrayList<>();
        expenditureList.add(totalExpenditure);
        return Response.buildSuccess(expenditureList);
    }

    @Authorized(roles = {Role.FINANCIAL_STAFF, Role.ADMIN})
    @PostMapping(value = "/redFlush")
    public Response generateRedFlush(@RequestBody SheetVO sheetVO){
        sheetsReviewService.redFlush(sheetVO);
        return Response.buildSuccess();
    }
}
