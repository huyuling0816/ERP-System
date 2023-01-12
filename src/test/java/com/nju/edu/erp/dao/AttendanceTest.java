package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.AttendancePO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class AttendanceTest {
    @Autowired
    AttendanceDao attendanceDao;

    @Test
    @Transactional
    @Rollback(value = true)
    public void insert() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(new Date());
        AttendancePO attendancePO = AttendancePO.builder()
                .uid(1)
                .uname("chen")
                .build();
        try {
            attendancePO.setDate(dateFormat.parse(dateStr));
        } catch (Exception e){
            e.printStackTrace();
        }
        attendanceDao.insertAttendRecord(attendancePO);
        Integer countDayAttend = attendanceDao.countDayAttend(attendancePO.getDate(), attendancePO.getUid());
        Assertions.assertEquals(1, countDayAttend);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void countMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(new Date());
        AttendancePO attendancePO = AttendancePO.builder()
                .uid(1)
                .uname("chen")
                .build();
        try {
            attendancePO.setDate(dateFormat.parse(dateStr));
        } catch (Exception e){
            e.printStackTrace();
        }
        Integer countAttendRecord = attendanceDao.countAttendRecord(attendancePO.getDate(), attendancePO.getUid());
        Assertions.assertEquals(1, countAttendRecord);
        attendanceDao.insertAttendRecord(attendancePO);

        Integer countAttendRecord2 = attendanceDao.countAttendRecord(attendancePO.getDate(), attendancePO.getUid());
        Assertions.assertEquals(2, countAttendRecord2);
    }
}
