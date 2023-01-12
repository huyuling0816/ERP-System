package com.nju.edu.erp.model.vo.attend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceVO {
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
    String dateStr;
}
