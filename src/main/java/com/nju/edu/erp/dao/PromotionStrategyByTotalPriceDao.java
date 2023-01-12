package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PromotionStrategyByTotalPricePO;
import com.nju.edu.erp.model.po.PromotionStrategyByUserLevelPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface PromotionStrategyByTotalPriceDao {
    /**
     * 获取最近一条策略
     * @return
     */
    PromotionStrategyByTotalPricePO getLatestStrategy();

    /**
     * 存入一条促销策略记录（只是五条中的一条）
     *
     * @param toSave 一条促销策略记录
     * @return 影响的行数
     */
    int saveStrategy(PromotionStrategyByTotalPricePO toSave);

    /**
     * 返回记录的条数
     *
     * @return 记录的条数
     */
    int numOfRecords();

    List<PromotionStrategyByTotalPricePO> findAll();

    PromotionStrategyByTotalPricePO findStrategyByTotalPrice(BigDecimal totalPrice);
}
