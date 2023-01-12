package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.personnel.PersonnelVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonnelService {
    PersonnelVO createPersonnel(PersonnelVO personnelVO);

    PersonnelVO findById(Integer id);

    PersonnelVO findByName(String name);

    List<PersonnelVO> findAllPersonnel();

    PersonnelVO updatePersonnel(PersonnelVO personnelVO);

    void deleteById(Integer id);
}
