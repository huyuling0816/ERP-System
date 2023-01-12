package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.PaymentSheetContentPO;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface PaymentSheetDao {
    /**
     * 获取最近一条付款单
     * @return
     */
    PaymentSheetPO getLatestSheet();

    /**
     * 存入一条付款单记录
     * @param toSave 一条付款单记录
     * @return 影响的行数
     */
    int saveSheet(PaymentSheetPO toSave);

    /**
     * 把付款单上的具体内容存入数据库
     * @param paymentSheetContent 入付款单上的具体内容
     */
    int saveBatchSheetContent(List<PaymentSheetContentPO> paymentSheetContent);

    /**
     * 查找所有付款单
     */
    List<PaymentSheetPO> findAllSheet();

    List<PaymentSheetPO> findAllByState(FinanceSheetState state);

    List<PaymentSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<PaymentSheetPO> findAllByCustomerId(Integer id);

    List<PaymentSheetPO> findAllByOperatorName(String operator);

    /**
     * 查找指定id的付款单
     * @param id
     * @return
     */
    PaymentSheetPO findSheetById(String id);

    /**
     * 查找指定付款单下具体的内容
     * @param sheetId
     */
    List<PaymentSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 更新指定销售单的状态
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, FinanceSheetState state);

    /**
     * 根据当前状态更新销售单状态
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, FinanceSheetState prev, FinanceSheetState state);

}
