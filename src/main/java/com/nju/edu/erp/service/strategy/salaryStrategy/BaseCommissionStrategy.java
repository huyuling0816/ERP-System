package com.nju.edu.erp.service.strategy.salaryStrategy;

import com.nju.edu.erp.dao.AttendanceDao;
import com.nju.edu.erp.dao.SaleSheetDao;
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

public class BaseCommissionStrategy implements SalaryCalculateStrategy {

    private final SalarySystemService salarySystemService;
    private final PersonnelService personnelService;

    private final BigDecimal commissionRate;

    private final SaleSheetDao saleSheetDao;

    private final AttendanceDao attendanceDao;

    private final BonusService bonusService;
    public BaseCommissionStrategy(SalarySystemService salarySystemService,PersonnelService personnelService,
                                  SaleSheetDao saleSheetDao,BigDecimal commissionRate,
                                  AttendanceDao attendanceDao,BonusService bonusService){
        this.salarySystemService=salarySystemService;
        this.personnelService=personnelService;
        this.commissionRate=commissionRate;
        this.saleSheetDao=saleSheetDao;
        this.attendanceDao=attendanceDao;
        this.bonusService=bonusService;
    }

    /**
     * 计算当月原工资，关于打卡按每人每月应最少工作25天计算,即打卡少于25次会扣工资,12月份会加上年终奖
     * @param uid
     * @param date
     * @return
     */
    @Override
    public BigDecimal calculateRawSalary(Integer uid,Date date){
        try {
            PersonnelVO personnelVO = personnelService.findById(uid);
            SalarySystemVO salarySystemVO = salarySystemService.findSalarySystem(personnelVO.getRole());
            BigDecimal base = salarySystemVO.getBase();
            BigDecimal post = salarySystemVO.getPost();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(date);
            BigDecimal finalAmount = saleSheetDao.getFinalAmountBySalesman(personnelVO.getName(),dateFormat.parse(today));
            BigDecimal commission;
            if (finalAmount == null) {
                commission = BigDecimal.ZERO;
            } else {
                commission = finalAmount.multiply(commissionRate);
            }
            int absenceTime = 25 - attendanceDao.countAttendRecord(dateFormat.parse(today), uid);     //当月缺勤次数
            BigDecimal absence=BigDecimal.ZERO;
            if(absenceTime>0) {
                absence = base.multiply(BigDecimal.valueOf(absenceTime)).multiply(BigDecimal.valueOf(0.033));
            }     //缺勤所扣工资
            BigDecimal res=base.add(post).add(commission).subtract(absence);
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
