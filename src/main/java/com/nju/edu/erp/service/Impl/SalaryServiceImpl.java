package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PersonnelDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.enums.salary.SalaryDistribution;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.po.PersonnelPO;
import com.nju.edu.erp.model.po.SalarySheetPO;
import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import com.nju.edu.erp.model.vo.salary.BonusVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.AccountService;
import com.nju.edu.erp.service.SalaryCalculateService;
import com.nju.edu.erp.service.SalaryService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalarySheetDao salarySheetDao;

    private final PersonnelDao personnelDao;

    private final AccountService accountService;

    private final SalaryCalculateService salaryCalculateService;

    @Autowired
    public SalaryServiceImpl(SalarySheetDao salarySheetDao,PersonnelDao personnelDao, AccountService accountService,
                             SalaryCalculateService salaryCalculateService){
        this.salarySheetDao = salarySheetDao;
        this.personnelDao = personnelDao;
        this.accountService = accountService;
        this.salaryCalculateService=salaryCalculateService;
    }

    @Override
    @Transactional
    public void makeSalarySheet(SalarySheetVO salarySheetVO) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SalarySheetPO salarySheetPO = new SalarySheetPO();
            BeanUtils.copyProperties(salarySheetVO, salarySheetPO);
            String dateStr = salarySheetVO.getDateStr();
            Date date = dateFormat.parse(dateStr);
            salarySheetPO.setDate(date);
            salarySheetPO.setState(SalarySheetState.PENDING);
            SalarySheetPO latest = salarySheetDao.getLatestSheet();
            String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZD");
            salarySheetPO.setId(id);
            BigDecimal actualSalary = salarySheetPO.getRawSalary().subtract(salarySheetPO.getPersonalIncomeTax()).subtract(salarySheetPO.getHousingProvidentFund()).subtract(salarySheetPO.getUnemploymentInsurance());
            salarySheetPO.setActualSalary(actualSalary);
            if (personnelDao.findByName(salarySheetVO.getUname()) == null) {
                throw new RuntimeException("此员工不存在！");
            }
            salarySheetDao.createSalarySheet(salarySheetPO);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void makeAllSalarySheet(){
        try {
            List<PersonnelPO> personnelPOS = personnelDao.findAll();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String temp = dateFormat.format(new Date());
            Date today = dateFormat.parse(temp);
            for (PersonnelPO personnelPO : personnelPOS) {
                PersonnelVO personnelVO = new PersonnelVO();
                BeanUtils.copyProperties(personnelPO, personnelVO);
                SalarySheetVO salarySheetVO = salaryCalculateService.makeSimpleSalarySheet(personnelPO.getId(), today);
                makeSalarySheet(salarySheetVO);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }




    @Override
    @Transactional
    public SalarySheetVO makeSimpleSalarySheet(Integer uid){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String temp = dateFormat.format(new Date());
            Date today = dateFormat.parse(temp);
            SalarySheetVO salarySheetVO = salaryCalculateService.makeSimpleSalarySheet(uid, today);
            return salarySheetVO;
        }catch (ParseException e){
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    @Transactional
    public List<SalarySheetVO> getSalaryServiceByState(SalarySheetState state) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SalarySheetPO> salarySheetPOS = salarySheetDao.getSalarySheetByState(state);
        if(salarySheetPOS == null) return null;
        List<SalarySheetVO> res = new ArrayList<>();
        for(SalarySheetPO salarySheetPO : salarySheetPOS) {
            SalarySheetVO salarySheetVO = new SalarySheetVO();
            BeanUtils.copyProperties(salarySheetPO,salarySheetVO);
            Date date=salarySheetPO.getDate();
            String dateStr=dateFormat.format(date);
            salarySheetVO.setDateStr(dateStr);
            res.add(salarySheetVO);
        }
        return res;
    }

    @Override
    @Transactional
    public List<SalarySheetVO> getAllSalarySheet(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SalarySheetPO> salarySheetPOS=salarySheetDao.getAllSalarySheet();
        List<SalarySheetVO> res=new ArrayList<SalarySheetVO>();
        for (SalarySheetPO salarySheetPO:salarySheetPOS){
            SalarySheetVO salarySheetVO=new SalarySheetVO();
            BeanUtils.copyProperties(salarySheetPO,salarySheetVO);
            Date date=salarySheetPO.getDate();
            String dateStr=dateFormat.format(date);
            salarySheetVO.setDateStr(dateStr);
            res.add(salarySheetVO);
        }
        return res;
    }

    @Override
    @Transactional
    public BigDecimal getYearActualSalary(Integer uid, Date date){
        return salarySheetDao.getYearActualSalary(uid, date);
    }
}
