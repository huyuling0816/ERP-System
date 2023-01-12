package com.nju.edu.erp.model.vo.promotionStrategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionStrategyByPricePacksVO {
    /**
     * 促销策略id（格式为：CXCLP-yyyyMMdd-xxxxx）
     */
    String sid;
    /**
     * 商品1的id
     */
    String pid1;
    /**
     * 商品1的数量
     */
    Integer number1;
    /**
     * 商品2的id
     */
    String pid2;
    /**
     * 商品2的数量
     */
    Integer number2;
    /**
     * 降价金额
     */
    BigDecimal reducePrice;
    Date beginTime;
    Date endTime;
}
