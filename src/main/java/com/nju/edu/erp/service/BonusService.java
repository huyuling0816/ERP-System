package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.BonusPO;
import com.nju.edu.erp.model.vo.salary.BonusVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public interface BonusService {
    BonusPO searchSalary(BonusVO bonusVO);

    void makeBonus(BonusVO bonusVO);

    List<BonusPO> getBonusList();

    BigDecimal getBonus(Integer uid, Date date);
}
