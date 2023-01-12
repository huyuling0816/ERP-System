package com.nju.edu.erp.service.strategy.approvalStrategy;

import com.nju.edu.erp.dao.GiftSheetDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.GiftSheetPO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftSheetApproval implements ApprovalStrategy{
    private final GiftSheetDao giftSheetDao;
    private final ProductService productService;

    @Autowired
    public GiftSheetApproval(GiftSheetDao giftSheetDao, ProductService productService) {
        this.giftSheetDao = giftSheetDao;
        this.productService = productService;
    }

    @Override
    public void approval(String SheetId, BaseEnum state) {
        if(state.equals(GiftSheetState.FAILURE)) {
            GiftSheetPO giftSheet = giftSheetDao.findSheetById(SheetId);
            if(giftSheet.getState() == GiftSheetState.FAILURE) throw new RuntimeException("状态更新失败");
            int effectLines = giftSheetDao.updateSheetState(SheetId, (GiftSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            GiftSheetState prevState;
            if(state.equals(GiftSheetState.SUCCESS)) {
                prevState = GiftSheetState.PENDING;
            }else{
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = giftSheetDao.updateSheetStateOnPrev(SheetId, prevState, (GiftSheetState) state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(GiftSheetState.SUCCESS)){
                // 更新商品数量
                GiftSheetPO giftSheetPO = giftSheetDao.findSheetById(SheetId);
                ProductInfoVO productInfoVO = productService.getOneProductByPid(giftSheetPO.getPid());
                if(productInfoVO.getQuantity() >= giftSheetPO.getNumber()) { // 防御式编程
                    productInfoVO.setQuantity(productInfoVO.getQuantity()-giftSheetPO.getNumber());
                }else {
                    productInfoVO.setQuantity(0);
                }
                productService.updateProduct(productInfoVO);
            }
        }
    }
}
