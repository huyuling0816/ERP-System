package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AccountDao;
import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ReceiptSheetDao;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.AccountVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetContentVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ReceiptService;
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
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptSheetDao receiptSheetDao;

    private final CustomerService customerService;

    private final AccountService accountService;


    @Autowired
    public ReceiptServiceImpl(ReceiptSheetDao receiptSheetDao, CustomerService customerService, AccountService accountService) {
        this.receiptSheetDao = receiptSheetDao;
        this.customerService = customerService;
        this.accountService = accountService;
    }


    @Override
    @Transactional
    public void makeReceiptSheet(UserVO userVO, ReceiptSheetVO receiptSheetVO) {
        ReceiptSheetPO receiptSheetPO = new ReceiptSheetPO();
        BeanUtils.copyProperties(receiptSheetVO, receiptSheetPO);
        receiptSheetPO.setOperator(userVO.getName());
        receiptSheetPO.setCreateTime(new Date());
        ReceiptSheetPO latest = receiptSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
        receiptSheetPO.setId(id);
        receiptSheetPO.setState(FinanceSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<ReceiptSheetContentPO> rContentPOList = new ArrayList<>();
        for (ReceiptSheetContentVO content : receiptSheetVO.getReceiptSheetContent()) {
            ReceiptSheetContentPO rContentPO = new ReceiptSheetContentPO();
            BeanUtils.copyProperties(content, rContentPO);
            rContentPO.setReceiptSheetId(id);
            BigDecimal amount = rContentPO.getTransferAmount();
            totalAmount = totalAmount.add(amount);
            rContentPOList.add(rContentPO);
        }
        receiptSheetDao.saveBatchSheetContent(rContentPOList);
        receiptSheetPO.setTotalAmount(totalAmount);
        receiptSheetDao.saveSheet(receiptSheetPO);
    }

    @Transactional
    @Override
    public List<ReceiptSheetVO> getReceiptSheetByState(FinanceSheetState state) {
        List<ReceiptSheetVO> res = new ArrayList<>();
        List<ReceiptSheetPO> all;
        if(state == null){
            all = receiptSheetDao.findAllSheet();
        } else {
            all = receiptSheetDao.findAllByState(state);
        }
        for(ReceiptSheetPO po : all){
            ReceiptSheetVO vo = new ReceiptSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<ReceiptSheetContentPO> alll = receiptSheetDao.findContentBySheetId(po.getId());
            List<ReceiptSheetContentVO> vos = new ArrayList<>();
            for (ReceiptSheetContentPO p : alll){
                ReceiptSheetContentVO v = new ReceiptSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setReceiptSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Transactional
    @Override
    public ReceiptSheetVO getReceiptSheetById(String receiptSheetId) {
        ReceiptSheetPO receiptSheetPO = receiptSheetDao.findSheetById(receiptSheetId);
        if(receiptSheetPO == null)  return null;
        List<ReceiptSheetContentPO> contentPO = receiptSheetDao.findContentBySheetId(receiptSheetId);
        ReceiptSheetVO rVO = new ReceiptSheetVO();
        BeanUtils.copyProperties(receiptSheetPO, rVO);
        List<ReceiptSheetContentVO> receiptSheetContentVOList = new ArrayList<>();
        for (ReceiptSheetContentPO content : contentPO){
            ReceiptSheetContentVO rContentVO = new ReceiptSheetContentVO();
            BeanUtils.copyProperties(content, rContentVO);
            receiptSheetContentVOList.add(rContentVO);
        }
        rVO.setReceiptSheetContent(receiptSheetContentVOList);
        return rVO;
    }

    @Transactional
    @Override
    public List<ReceiptSheetPO> findAllByCreateTime(Date beginTime, Date endTime) {
        return receiptSheetDao.findAllByCreateTime(beginTime, endTime);
    }

    @Transactional
    @Override
    public List<ReceiptSheetPO> findAllSheet() {
        return receiptSheetDao.findAllSheet();
    }

    @Transactional
    @Override
    public List<ReceiptSheetPO> findAllByCustomerId(Integer id){
        return receiptSheetDao.findAllByCustomerId(id);
    }

    @Transactional
    @Override
    public List<ReceiptSheetPO> findAllByOperatorName(String operator){
        return receiptSheetDao.findAllByOperatorName(operator);
    }
}
