package com.nju.edu.erp.model.po;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftSheetPO {
    /**
     * 赠送单id（格式为：ZSD-yyyyMMdd-xxxxx）
     */
    String id;
    /**
     * 关联的销售单id
     */
    String saleSheetId;
    /**
     * 商品id
     */
    String pid;
    /**
     * 商品数量
     */
    Integer number;
    /**
     * 商品单价
     */
    BigDecimal unitPrice;
    /**
     * 总价
     */
    BigDecimal totalPrice;

    GiftSheetState state;

}
