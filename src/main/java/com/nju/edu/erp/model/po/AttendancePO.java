package com.nju.edu.erp.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendancePO {
    /**
     * 单号
     */
    Integer id;

    /**
     * 员工id
     */
    Integer uid;

    /**
     * 姓名
     */
    String uname;


    /**
     * 打卡日期
     */
    Date date;
}
