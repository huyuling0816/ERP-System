package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyByPricePacksPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PromotionStrategyByPricePacksDao {
    /**
     * 获取最近一条策略
     *
     * @return
     */
    PromotionStrategyByPricePacksPO getLatestSheet();

    /**
     * 保存策略
     * @param promotionStrategyByPricePacksPO
     */
    void saveStrategy(PromotionStrategyByPricePacksPO promotionStrategyByPricePacksPO);

    List<PromotionStrategyByPricePacksPO> findAll();
}
