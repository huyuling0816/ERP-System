package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.PaymentSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public interface PaymentService {
    /**
     * 指定付款单
     * @param userVO
     * @param paymentSheetVO
     */
    void makePaymentSheet(UserVO userVO, PaymentSheetVO paymentSheetVO);

    /**
     * 根据单据状态获取付款单
     * @param state
     * @return
     */
    List<PaymentSheetVO> getPaymentSheetByState(FinanceSheetState state);

    /**
     * 根据付款单Id搜索付款单信息
     * @param paymentSheetId 付款单Id
     * @return 付款单全部信息
     */
    PaymentSheetVO getPaymentSheetById(String paymentSheetId);

    List<PaymentSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<PaymentSheetPO> findAllSheet();

    List<PaymentSheetPO> findAllByCustomerId(Integer id);

    List<PaymentSheetPO> findAllByOperatorName(String operator);
}
