package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.ReceiptSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ReceiptService {
    /**
     * 制定收款单
     * @param userVO
     * @param receiptSheetVO
     */
    void makeReceiptSheet(UserVO userVO, ReceiptSheetVO receiptSheetVO);

    /**
     * 根据单据状态获取收款单
     * @param state
     * @return
     */
    List<ReceiptSheetVO> getReceiptSheetByState(FinanceSheetState state);
    /**
     * 根据收款单Id搜索收款单信息
     * @param receiptSheetId 收款单Id
     * @return 收款单全部信息
     */
    ReceiptSheetVO getReceiptSheetById(String receiptSheetId);

    List<ReceiptSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<ReceiptSheetPO> findAllSheet();

    List<ReceiptSheetPO> findAllByCustomerId(Integer id);

    List<ReceiptSheetPO> findAllByOperatorName(String operator);

}
