package com.nju.edu.erp.model.po;

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
public class PromotionStrategyByTotalPricePO {
    /**
     * 促销策略id（格式为：CXCLT-yyyyMMdd-xxxxx）
     */
    String sid;
    /**
     * 满足优惠所需的总价
     */
    BigDecimal totalPrice;
    /**
     * 代金券
     */
    BigDecimal voucherAmount;
    /**
     * 赠品id
     */
    String gid;
    /**
     * 赠品数
     */
    Integer numOfGift;

    Date beginTime;

    Date endTime;
}
