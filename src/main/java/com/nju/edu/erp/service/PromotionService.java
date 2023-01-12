package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByPricePacksVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByTotalPriceVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByUserLevelVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PromotionService {

    /**
     * 根据用户级别制定销售策略
     * @param list
     */
    void makePromotionStrategyByUserLevel(List<PromotionStrategyByUserLevelVO> list);
    /**
     * 制定特价包
     * @param promotionStrategyByPricePacksVO
     */
    void makePromotionStrategyByPricePacks(PromotionStrategyByPricePacksVO promotionStrategyByPricePacksVO);
    /**
     * 根据总价制定销售策略
     * @param list
     */
    void makePromotionStrategyByTotalPrice(List<PromotionStrategyByTotalPriceVO> list);
    /**
     * 返回所有用户级别促销策略
     * @return List<PromotionStrategyByUserLevelVO>
     */
    List<PromotionStrategyByUserLevelVO> findAllPromotionStrategyByUserLevel();
    /**
     * 返回所有特价包策略
     * @return List<PromotionStrategyByPricePacksVO>
     */
    List<PromotionStrategyByPricePacksVO> findAllPromotionStrategyByPricePacks();
    /**
     * 返回所有总价策略
     * @return List<PromotionStrategyByTotalPriceVO>
     */
    List<PromotionStrategyByTotalPriceVO> findAllPromotionStrategyByTotalPrice();
}
