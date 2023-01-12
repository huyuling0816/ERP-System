package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.po.SaleDetailPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategy;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public interface SaleService {

    /**
     * 指定销售单
     *
     * @param userVO
     * @param saleSheetVO
     */
    void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO, List<PromotionStrategy> promotionStrategyList);

    /**
     * 根据单据状态获取销售单
     *
     * @param state
     * @return
     */
    List<SaleSheetVO> getSaleSheetByState(SaleSheetState state);

    /**
     * 审批单据
     *
     * @param saleSheetId
     * @param state
     */
    void approval(String saleSheetId, SaleSheetState state);

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     *
     * @param salesman     销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return
     */
    CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman, String beginDateStr, String endDateStr);

    /**
     * 根据销售单Id搜索销售单信息
     *
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    SaleSheetVO getSaleSheetById(String saleSheetId);

    List<SaleDetailPO> getSaleDetailByTime(String beginDateStr, String endDateStr);

    List<SaleDetailPO> getSaleDetailByProduct(String productName);

    List<SaleDetailPO> getSaleDetailByCustomer(String customerName);

    List<SaleDetailPO> getSaleDetailByOperator(String operatorName);

    List<SaleDetailPO> getSaleDetailByWarehouse(String warehouseId);

    List<SaleSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<SaleSheetPO> findAllSheet();

    List<SaleSheetPO> findAllByCustomerId(Integer id);

    List<SaleSheetPO> findAllByOperatorName(String operator);

}
