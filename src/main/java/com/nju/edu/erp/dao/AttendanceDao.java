package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AttendancePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface AttendanceDao {
    int insertAttendRecord(AttendancePO attendancePO);

    /**
     * 删除日期之前的所有打卡记录，用于清除上个月的记录
     * @param date
     * @return
     */
    int deletePreRecord(Date date);

    /**
     * 查询员工当月打卡次数
     * @param date
     * @param uid
     * @return
     */
    Integer countAttendRecord(Date date,Integer uid);


    /**
     * 查询员工当日打卡情况
     * @param date
     * @param uid
     * @return
     */
    Integer countDayAttend(Date date,Integer uid);

    List<AttendancePO> findAll();
}
