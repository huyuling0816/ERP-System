package com.nju.edu.erp.service.strategy.promotionStrategy;

import com.nju.edu.erp.dao.PromotionStrategyByTotalPriceDao;
import com.nju.edu.erp.model.po.GiftSheetPO;
import com.nju.edu.erp.model.po.PromotionStrategyByTotalPricePO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import com.nju.edu.erp.service.GiftSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PromotionStrategyByTotalPrice implements PromotionStrategy {

    private final PromotionStrategyByTotalPriceDao promotionStrategyByTotalPriceDao;

    private final GiftSheetService giftSheetService;

    @Autowired
    public PromotionStrategyByTotalPrice(PromotionStrategyByTotalPriceDao promotionStrategyByTotalPriceDao, GiftSheetService giftSheetService) {

        this.promotionStrategyByTotalPriceDao = promotionStrategyByTotalPriceDao;

        this.giftSheetService = giftSheetService;

    }

    @Override
    public void updateSaleSheet(SaleSheetPO saleSheetPO) {
        // 因为只有在一条策略过期后才能制定下一条策略，所以只有最后的策略记录才可能是有效的
        PromotionStrategyByTotalPricePO latest = promotionStrategyByTotalPriceDao.getLatestStrategy();
        // 获取可能有效记录的策略编号 开始时间 结束时间
        String sid = latest.getSid();
        Date beginTime = latest.getBeginTime(), endTime = latest.getEndTime();
        // 判断此条策略是否有效
        Date current = new Date();
        if((current.after(beginTime)&&current.before(endTime))||current.equals(beginTime)||current.equals(endTime)){ // 有效，
            PromotionStrategyByTotalPricePO po = promotionStrategyByTotalPriceDao.findStrategyByTotalPrice(saleSheetPO.getRawTotalAmount());
            if(po.getVoucherAmount() != null) {
                saleSheetPO.setVoucherAmount(saleSheetPO.getVoucherAmount().add(po.getVoucherAmount()));
            }
            if(po.getGid()!=null){
                String gid = po.getGid();
                int num = po.getNumOfGift();
                GiftSheetPO sheet = new GiftSheetPO();
                sheet.setSaleSheetId(saleSheetPO.getId());
                sheet.setNumber(num);
                sheet.setPid(gid);
                giftSheetService.makeGiftSheet(sheet);
            }
        }
    }
}
