package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.SaleDetailPO;
import com.nju.edu.erp.model.vo.operationSheet.Expenditure;
import com.nju.edu.erp.model.vo.operationSheet.IncomeAfterDiscount;
import com.nju.edu.erp.model.vo.OperationSheetVO;
import com.nju.edu.erp.model.vo.SheetVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Service
public interface SheetsReviewService {
    /**
     * 查看销售明细表：设定一个时间段，查看此时间段内的销售/销售退货的时间/商品名/型号/数量/单价/总额
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     * @throws ParseException
     */
    List<SaleDetailPO> getSaleDetailByTime(String beginDateStr, String endDateStr);

    /**
     * 查看销售明细表：设定一个商品，查看包含某商品的销售/销售退货的时间/商品名/型号/数量/单价/总额
     * @param productName 商品名
     */
    List<SaleDetailPO> getSaleDetailByProduct(String productName);

    /**
     * 查看销售明细表，查看关于某客户的销售/销售退货的时间/商品名/型号/数量/单价/总额
     * @param customerName 客户名
     */
    List<SaleDetailPO> getSaleDetailByCustomer(String customerName);

    /**
     * 查看销售明细表，查看关于某操作员的销售/销售退货的时间/商品名/型号/数量/单价/总额
     * @param operatorName 操作员
     */
    List<SaleDetailPO> getSaleDetailByOperator(String operatorName);

    /**
     * 查看销售明细表，查看包含某仓库内商品的销售/销售退货的时间/商品名/型号/数量/单价/总额
     * @param warehouseId 仓库
     */
    List<SaleDetailPO> getSaleDetailByWarehouse(String warehouseId);

    /**
     * 销售收入、商品类收入（商品报溢收入,成本调价收入,进货退货差价,代金券与实际收款差额收入）
     * @return 折让后的总收入
     */
    IncomeAfterDiscount getIncomeAfterDiscount();

    /**
     * 销售收入、商品类收入（商品报溢收入,成本调价收入,进货退货差价,代金券与实际收款差额收入）的总折让
     * @return 总收入的总折让
     */
    BigDecimal getTotalIncomeDiscount();

    /**
     * 利润：折让后总收入-总支出
     */
    BigDecimal getTotalProfit();

    /**
     * 支出类：销售成本、商品类支出（商品报损商品赠出）、人力成本。支出类显示总支出。
     * @return
     */
    Expenditure getTotalExpenditure();

    /**
     * 查看经营情况表
     * 统计显示一段时间内的经营收支状况和利润。经营收入显示为折让后，并显示出折让了多少。
     * @return
     */
    OperationSheetVO getOperationSheet();

    /**
     * 按时间查看经营历程表（查看一段时间里的所有单据）
     */
    List<SheetVO> getSheetByTime(String beginDateStr, String endDateStr);

    /**
     * 按单据类型查看经营历程表（查看一段时间里的所有单据）
     */
    List<SheetVO> getSheetBySheetType(String sheetType);

    /**
     * 按客户查看经营历程表（查看一段时间里的所有单据）
     */
    List<SheetVO> getSheetByCustomerName(String customerName);

    /**
     * 按业务员查看经营历程表（查看一段时间里的所有单据）
     */
    List<SheetVO> getSheetByOperatorName(String operatorName);

    /**
     * 红冲
     */
    void redFlush(SheetVO sheetVO);

    SheetVO redFlushAndCopy(SheetVO sheetVO);
}
