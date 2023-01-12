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
public class PromotionStrategyByUserLevelPO {
    /**
     * 促销策略id（格式为：CXCLL-yyyyMMdd-xxxxx）
     */
    String sid;
    /**
     * 用户等级
     */
    Integer level;
    /**
     * 折扣
     */
    BigDecimal discount;
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
