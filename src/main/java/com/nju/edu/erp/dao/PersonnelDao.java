package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.PersonnelPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PersonnelDao {
    int createPersonnel(PersonnelPO personnelPO);

    int updateByName(PersonnelPO personnelPO);

    PersonnelPO findByName(String name);

    PersonnelPO findById(Integer id);

    List<PersonnelPO> findAll();

    int deleteById(Integer id);
}
