package com.nju.edu.erp.enums.salary;

import com.nju.edu.erp.enums.BaseEnum;


public enum SalaryMethod implements BaseEnum<SalaryMethod,String> {
    MONTHLY("月薪制"),
    BASE_COMMISSION("基本工资+提成"),
    YEARLY("年薪制");
    private final String value;
    SalaryMethod(String value){
        this.value=value;
    }
    @Override
    public String getValue() {
        return value;
    }
}
