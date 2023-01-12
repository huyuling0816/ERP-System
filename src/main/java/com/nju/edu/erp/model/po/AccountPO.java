package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPO {
    /**
     * 账户ID
     */
    private Integer id;
    /**
     * 账户名称
     */
    private String name;
    /**
     * 账户余额
     */
    private BigDecimal amount;
}
