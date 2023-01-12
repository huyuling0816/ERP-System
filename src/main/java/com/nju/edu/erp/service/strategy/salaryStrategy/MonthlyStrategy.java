package com.nju.edu.erp.service.strategy.salaryStrategy;

import com.nju.edu.erp.dao.AttendanceDao;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import com.nju.edu.erp.service.BonusService;
import com.nju.edu.erp.service.PersonnelService;
import com.nju.edu.erp.service.SalarySystemService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyStrategy implements SalaryCalculateStrategy {
    private final SalarySystemService salarySystemService;
    private final PersonnelService personnelService;

    private final AttendanceDao attendanceDao;

    private final BonusService bonusService;

    public MonthlyStrategy(SalarySystemService salarySystemService,PersonnelService personnelService,
                           AttendanceDao attendanceDao,BonusService bonusService){
        this.salarySystemService=salarySystemService;
        this.personnelService=personnelService;
        this.attendanceDao=attendanceDao;
        this.bonusService=bonusService;
    }


    @Override
    public BigDecimal calculateRawSalary(Integer uid,Date date){
        //TODO 减去缺勤扣的工资
        try {
            PersonnelVO personnelVO = personnelService.findById(uid);
            SalarySystemVO salarySystemVO = salarySystemService.findSalarySystem(personnelVO.getRole());
            BigDecimal base = salarySystemVO.getBase();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String today = dateFormat.format(date);
            int absenceTime = 25 - attendanceDao.countAttendRecord(dateFormat.parse(today), uid);     //当月缺勤次数
            BigDecimal absence=BigDecimal.ZERO;
            if(absenceTime>0) {
                absence = base.multiply(BigDecimal.valueOf(absenceTime)).multiply(BigDecimal.valueOf(0.033));
            }
            BigDecimal res=base.add(salarySystemVO.getPost()).subtract(absence);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            if(calendar.get(Calendar.YEAR)==Calendar.DECEMBER){
                res=res.add(bonusService.getBonus(uid,date));
            }
            return res;
        }catch (ParseException e){
            e.printStackTrace();
        }
        throw new RuntimeException("日期转换错误");
    }


}
