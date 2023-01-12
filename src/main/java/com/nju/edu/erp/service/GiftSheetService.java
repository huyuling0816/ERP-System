package com.nju.edu.erp.service;

import java.util.List;

import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.GiftSheetPO;
import com.nju.edu.erp.model.vo.promotionStrategy.GiftSheetVO;
import org.springframework.stereotype.Service;

@Service
public interface GiftSheetService {
    /**
     * 制定赠送单
     * @param giftSheetPO
     */
    void makeGiftSheet(GiftSheetPO giftSheetPO);
    /**
     * 根据单据状态获取赠送单
     * @param state
     * @return
     */
    List<GiftSheetVO> getGiftSheetByState(GiftSheetState state);

    List<GiftSheetPO> findAll();
}
