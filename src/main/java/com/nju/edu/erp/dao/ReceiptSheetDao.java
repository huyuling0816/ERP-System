package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.ReceiptSheetContentPO;
import com.nju.edu.erp.model.po.ReceiptSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface ReceiptSheetDao {
    /**
     * 获取最近一条收款单
     * @return
     */
    ReceiptSheetPO getLatestSheet();

    /**
     * 存入一条收款单记录
     * @param toSave 一条收款单记录
     * @return 影响的行数
     */
    int saveSheet(ReceiptSheetPO toSave);

    /**
     * 把收款单上的具体内容存入数据库
     * @param receiptSheetContent 入收款单上的具体内容
     */
    int saveBatchSheetContent(List<ReceiptSheetContentPO> receiptSheetContent);

    /**
     * 查找所有收款单
     */
    List<ReceiptSheetPO> findAllSheet();

    List<ReceiptSheetPO> findAllByState(FinanceSheetState state);

    List<ReceiptSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<ReceiptSheetPO> findAllByCustomerId(Integer id);

    List<ReceiptSheetPO> findAllByOperatorName(String operator);

    /**
     * 查找指定id的收款单
     * @param id
     * @return
     */
    ReceiptSheetPO findSheetById(String id);

    /**
     * 查找指定收款单下具体的内容
     * @param sheetId
     */
    List<ReceiptSheetContentPO> findContentBySheetId(String sheetId);

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
