package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AttendanceDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.AttendancePO;
import com.nju.edu.erp.model.vo.attend.AttendanceVO;
import com.nju.edu.erp.service.AttendanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceDao attendanceDao;

    @Autowired
    public AttendanceServiceImpl(AttendanceDao attendanceDao){
        this.attendanceDao=attendanceDao;
    }

    @Override
    @Transactional
    public void addAttendRecord(AttendanceVO attendanceVO){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            AttendancePO attendancePO = new AttendancePO();
            BeanUtils.copyProperties(attendanceVO, attendancePO);
            String dateStr = dateFormat.format(new Date());
            attendancePO.setDate(dateFormat.parse(dateStr));
            int ans=attendanceDao.insertAttendRecord(attendancePO);
            if(ans==0){
                throw new MyServiceException("AttendError0001","签到失败！");
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void deletePreRecord(String dateStr){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date=dateFormat.parse(dateStr);
            attendanceDao.deletePreRecord(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }


    /**
     * 查询员工当月打卡次数
     * @param uid
     * @return
     */
    @Override
    @Transactional
    public int countMonthRecord(Integer uid){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(new Date());
            Date date=dateFormat.parse(dateStr);
            return attendanceDao.countAttendRecord(date,uid);
        }catch (ParseException e){
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    /**
     * 查询员工当天是否打卡
     * @param uid
     * @return
     */
    @Override
    @Transactional
    public int countDayAttend(Integer uid){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(new Date());
            Date date=dateFormat.parse(dateStr);
            return attendanceDao.countDayAttend(date, uid);
        }catch (ParseException e){
            e.printStackTrace();
        }
        throw new RuntimeException();
    }


    @Override
    @Transactional
    public List<AttendanceVO> findAll(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<AttendancePO> attendancePOS=attendanceDao.findAll();
        List<AttendanceVO> res=new ArrayList<>();
        for(AttendancePO attendancePO:attendancePOS){
            AttendanceVO attendanceVO=new AttendanceVO();
            BeanUtils.copyProperties(attendancePO,attendanceVO);
            String dateStr=dateFormat.format(attendancePO.getDate());
            attendanceVO.setDateStr(dateStr);
            res.add(attendanceVO);
        }
        return res;
    }
}
