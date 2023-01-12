package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.BonusPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface BonusDao {

    int createBonus(BonusPO bonusPO);

    int makeSalary(BonusPO bonusPO);

    List<BonusPO> getBonusList();

    BigDecimal getBonus(BonusPO bonusPO);
}
