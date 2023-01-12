package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PaymentSheetDao;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetContentVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.PaymentService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentSheetDao paymentSheetDao;

    private final AccountService accountService;

    private final CustomerService customerService;

    @Autowired
    public PaymentServiceImpl(PaymentSheetDao paymentSheetDao, AccountService accountService, CustomerService customerService) {
        this.paymentSheetDao = paymentSheetDao;
        this.accountService = accountService;
        this.customerService = customerService;
    }


    @Override
    @Transactional
    public void makePaymentSheet(UserVO userVO, PaymentSheetVO paymentSheetVO) {
        PaymentSheetPO paymentSheetPO = new PaymentSheetPO();
        BeanUtils.copyProperties(paymentSheetVO, paymentSheetPO);
        paymentSheetPO.setOperator(userVO.getName());
        paymentSheetPO.setCreateTime(new Date());
        PaymentSheetPO latest = paymentSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "FKD");
        paymentSheetPO.setId(id);
        paymentSheetPO.setState(FinanceSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PaymentSheetContentPO> rContentPOList = new ArrayList<>();
        for (PaymentSheetContentVO content : paymentSheetVO.getPaymentSheetContent()) {
            PaymentSheetContentPO rContentPO = new PaymentSheetContentPO();
            BeanUtils.copyProperties(content, rContentPO);
            rContentPO.setPaymentSheetId(id);
            BigDecimal amount = rContentPO.getTransferAmount();
            totalAmount = totalAmount.add(amount);
            rContentPOList.add(rContentPO);
        }
        paymentSheetDao.saveBatchSheetContent(rContentPOList);
        paymentSheetPO.setTotalAmount(totalAmount);
        paymentSheetDao.saveSheet(paymentSheetPO);
    }

    @Transactional
    @Override
    public List<PaymentSheetVO> getPaymentSheetByState(FinanceSheetState state) {
        List<PaymentSheetVO> res = new ArrayList<>();
        List<PaymentSheetPO> all;
        if(state == null){
            all = paymentSheetDao.findAllSheet();
        } else {
            all = paymentSheetDao.findAllByState(state);
        }
        for(PaymentSheetPO po : all){
            PaymentSheetVO vo = new PaymentSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PaymentSheetContentPO> alll = paymentSheetDao.findContentBySheetId(po.getId());
            List<PaymentSheetContentVO> vos = new ArrayList<>();
            for (PaymentSheetContentPO p : alll){
                PaymentSheetContentVO v = new PaymentSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPaymentSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Transactional
    @Override
    public PaymentSheetVO getPaymentSheetById(String paymentSheetId) {
        PaymentSheetPO paymentSheetPO = paymentSheetDao.findSheetById(paymentSheetId);
        if(paymentSheetPO == null)  return null;
        List<PaymentSheetContentPO> contentPO = paymentSheetDao.findContentBySheetId(paymentSheetId);
        PaymentSheetVO rVO = new PaymentSheetVO();
        BeanUtils.copyProperties(paymentSheetPO, rVO);
        List<PaymentSheetContentVO> paymentSheetContentVOList = new ArrayList<>();
        for (PaymentSheetContentPO content : contentPO){
            PaymentSheetContentVO rContentVO = new PaymentSheetContentVO();
            BeanUtils.copyProperties(content, rContentVO);
            paymentSheetContentVOList.add(rContentVO);
        }
        rVO.setPaymentSheetContent(paymentSheetContentVOList);
        return rVO;
    }

    @Transactional
    @Override
    public List<PaymentSheetPO> findAllByCreateTime(Date beginTime, Date endTime) {
        return paymentSheetDao.findAllByCreateTime(beginTime, endTime);
    }

    @Transactional
    @Override
    public List<PaymentSheetPO> findAllSheet() {
        return paymentSheetDao.findAllSheet();
    }

    @Transactional
    @Override
    public List<PaymentSheetPO> findAllByCustomerId(Integer id){
        return paymentSheetDao.findAllByCustomerId(id);
    }

    @Transactional
    @Override
    public List<PaymentSheetPO> findAllByOperatorName(String operator){
        return paymentSheetDao.findAllByOperatorName(operator);
    }
}
