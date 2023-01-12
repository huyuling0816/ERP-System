package com.nju.edu.erp.service.strategy.promotionStrategy;

import com.nju.edu.erp.model.po.SaleSheetPO;
import org.springframework.stereotype.Service;

@Service
public interface PromotionStrategy {

    void updateSaleSheet(SaleSheetPO saleSheetPO);

}
