package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.AttendanceDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.salary.SalaryDistribution;
import com.nju.edu.erp.enums.salary.SalaryMethod;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySystemVO;
import com.nju.edu.erp.service.BonusService;
import com.nju.edu.erp.service.PersonnelService;
import com.nju.edu.erp.service.SalaryCalculateService;
import com.nju.edu.erp.service.SalarySystemService;
import com.nju.edu.erp.service.strategy.salaryStrategy.BaseCommissionStrategy;
import com.nju.edu.erp.service.strategy.salaryStrategy.MonthlyStrategy;
import com.nju.edu.erp.service.strategy.salaryStrategy.SalaryCalculateStrategy;
import com.nju.edu.erp.service.strategy.salaryStrategy.YearlyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class SalaryCalculateServiceImpl implements SalaryCalculateService {

    private final SalarySystemService salarySystemService;

    private final PersonnelService personnelService;

    private SalaryCalculateStrategy salaryCalculateStrategy;

    private final SaleSheetDao saleSheetDao;

    private final AttendanceDao attendanceDao;

    private final BonusService bonusService;

    @Autowired
    public SalaryCalculateServiceImpl(SalarySystemService salarySystemService, PersonnelService personnelService,
                                      SaleSheetDao saleSheetDao,AttendanceDao attendanceDao,BonusService bonusService){
        this.salarySystemService=salarySystemService;
        this.personnelService=personnelService;
        this.saleSheetDao=saleSheetDao;
        this.attendanceDao=attendanceDao;
        this.bonusService=bonusService;
    }


    /**
     * 策略模式
     * @param uid
     * @param date  制定工资单日期 yyyy-mm-dd
     * @return
     */
    @Override
    @Transactional
    public SalarySheetVO makeSimpleSalarySheet(Integer uid, Date date){
        //TODO  如果是12月份,给总经理制定工资单，所有员工工资加上年终奖
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SalarySheetVO res=new SalarySheetVO();
        PersonnelVO personnelVO=personnelService.findById(uid);
        Role role=personnelVO.getRole();
        SalarySystemVO salarySystemVO=salarySystemService.findSalarySystem(role);
        SalaryMethod salaryMethod=salarySystemVO.getSalaryMethod();
        SalaryDistribution salaryDistribution=salarySystemVO.getSalaryDistribution();
        if(salaryDistribution==SalaryDistribution.YEAR){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            if(calendar.get(Calendar.YEAR)==Calendar.DECEMBER) {
                if(salaryMethod==SalaryMethod.YEARLY) {
                    salaryCalculateStrategy = new YearlyStrategy(salarySystemService, personnelService);
                }
            }else {
                throw new MyServiceException("Salary0001","目前不是12月，不需要给总经理制定工资单！");
            }
        }else {
            if (salaryMethod == SalaryMethod.MONTHLY) {
                salaryCalculateStrategy = new MonthlyStrategy(salarySystemService, personnelService, attendanceDao,bonusService);
            } else if (salaryMethod == SalaryMethod.BASE_COMMISSION) {
                salaryCalculateStrategy = new BaseCommissionStrategy(salarySystemService, personnelService, saleSheetDao, BigDecimal.valueOf(0.001), attendanceDao,bonusService);
            }
        }
        BigDecimal rawSalary=salaryCalculateStrategy.calculateRawSalary(uid,date);
        BigDecimal unemploymentInsurance=calculateUnemploymentInsurance(rawSalary,role);
        BigDecimal hosingFund=calculateHosingFund(rawSalary,role);
        BigDecimal tax=calculateTax(rawSalary.subtract(unemploymentInsurance).subtract(hosingFund));
        res.setUid(uid);
        res.setUname(personnelVO.getName());
        res.setAccountName(personnelVO.getCardNumber());     //AccountName银行卡号
        res.setRawSalary(salaryCalculateStrategy.calculateRawSalary(uid,date));
        res.setUnemploymentInsurance(unemploymentInsurance);
        res.setHousingProvidentFund(hosingFund);
        res.setPersonalIncomeTax(tax);
        res.setActualSalary(calculateActualSalary(rawSalary,unemploymentInsurance,hosingFund,tax));
        res.setDateStr(dateFormat.format(date));
        res.setState(SalarySheetState.PENDING);
        return res;
    }

    private BigDecimal calculateUnemploymentInsurance(BigDecimal rawSalary, Role role){
        SalarySystemVO salarySystemVO=salarySystemService.findSalarySystem(role);
        return calculate(rawSalary,salarySystemVO.getUnemploymentInsuranceRate());
    }

    private BigDecimal calculateHosingFund(BigDecimal rawSalary,Role role){
        SalarySystemVO salarySystemVO=salarySystemService.findSalarySystem(role);
        return calculate(rawSalary,salarySystemVO.getHousingFundRate());
    }

    private BigDecimal calculate(BigDecimal rawSalary,BigDecimal rate){
        return rawSalary.multiply(rate);
    }

    /**
     * 表驱动
     * @param Salary 减去五险一金后的值
     * @return
     */

    private BigDecimal calculateTax(BigDecimal Salary){
        final int[] level={5000,36000,144000,300000,420000,660000,960000};
        final double[] taxRate={0.03,0.10,0.20,0.25,0.30,0.35,0.45};
        final int[] temp={0,2520,16920,31920,52920,85920,181920};    //速算扣除数
        int index=-1;
        for(int i=0;i<level.length;i++){
            if(Salary.compareTo(BigDecimal.valueOf(level[i]))>0){
                index++;
            }else {
                break;
            }
        }
        if(index==-1){
            return BigDecimal.valueOf(0);
        }else {
            return Salary.subtract(BigDecimal.valueOf(5000)).multiply(BigDecimal.valueOf(taxRate[index])).subtract(BigDecimal.valueOf(temp[index]));
        }
    }

    private BigDecimal calculateActualSalary(BigDecimal rawSalary,BigDecimal unemploymentInsurance,BigDecimal housingFund,BigDecimal tax){
        return rawSalary.subtract(unemploymentInsurance).subtract(housingFund).subtract(tax);
    }
}
