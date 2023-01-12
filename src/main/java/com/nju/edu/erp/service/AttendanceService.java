package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.attend.AttendanceVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface AttendanceService {
    void addAttendRecord(AttendanceVO attendanceVO);

    void deletePreRecord(String dateStr);

    int countMonthRecord(Integer uid);

    int countDayAttend(Integer uid);

    List<AttendanceVO> findAll();
}
