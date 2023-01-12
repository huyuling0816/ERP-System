package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.BonusDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.vo.salary.BonusVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.BonusService;
import com.nju.edu.erp.service.SalaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BonusServiceImpl implements BonusService {

    private final SalarySheetDao salarySheetDao;

    private final BonusDao bonusDao;

    @Autowired
    public BonusServiceImpl(SalarySheetDao salarySheetDao, BonusDao bonusDao){
        this.bonusDao = bonusDao;
        this.salarySheetDao = salarySheetDao;
    }

    @Override
    public BonusPO searchSalary(BonusVO bonusVO) {
        Integer yearInt = bonusVO.getYear();
        String dateString = yearInt + "-02-23";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateString);
            BigDecimal salary = salarySheetDao.getYearActualSalary(bonusVO.getUid(),date);
            bonusVO.setSalary(salary);
            BonusPO bonusPOSave = new BonusPO();
            BeanUtils.copyProperties(bonusVO,bonusPOSave);
            bonusDao.createBonus(bonusPOSave);
            return bonusPOSave;
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void makeBonus(BonusVO bonusVO) {
        BonusPO bonusPOSave = new BonusPO();
        BeanUtils.copyProperties(bonusVO,bonusPOSave);
        bonusDao.makeSalary(bonusPOSave);
    }

    @Override
    public List<BonusPO> getBonusList() {
        return bonusDao.getBonusList();
    }

    @Override
    public BigDecimal getBonus(Integer uid, Date date) {
        BonusPO bonusPO = new BonusPO();
        bonusPO.setUid(uid);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        bonusPO.setYear(year);
        return bonusDao.getBonus(bonusPO);
    }
}
