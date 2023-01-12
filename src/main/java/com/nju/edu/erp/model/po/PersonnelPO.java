package com.nju.edu.erp.model.po;


import com.nju.edu.erp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonnelPO {
    /**
     * 员工id,主码
     */
    private Integer id;


    /**
     * 员工姓名
     */
    private String name;


    /**
     * 性别
     */
    private char gender;


    /**
     * 出生日期
     */
    private String birthday;


    /**
     * 电话号码
     */
    private String phoneNumber;


    /**
     * 职位
     */
    private Role role;



    /**
     * 银行卡号
     */
    private String cardNumber;
}
