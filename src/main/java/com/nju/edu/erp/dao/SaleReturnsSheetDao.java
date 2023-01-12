package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.SaleSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface SaleReturnsSheetDao {
    /**
     * 获取最近一条销售退货单
     * @return 最近一条销售退货单
     */
    SaleReturnsSheetPO getLatest();

    /**
     * 存入一条销售退货单记录
     * @param toSave 一条销售退货单记录
     * @return 影响的行数
     */
    int save(SaleReturnsSheetPO toSave);

    /**
     * 把销售退货单上的具体内容存入数据库
     * @param saleReturnsSheetContent 销售退货单上的具体内容
     */
    void saveBatch(List<SaleReturnsSheetContentPO> saleReturnsSheetContent);

    /**
     * 返回所有销售退货单
     * @return 销售退货单列表
     */
    List<SaleReturnsSheetPO> findAll();


    List<SaleReturnsSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<SaleReturnsSheetPO> findAllByCustomerId(Integer id);

    List<SaleReturnsSheetPO> findAllByOperatorName(String operator);

    /**
     * 根据state返回销售退货单
     * @param state 销售退货单状态
     * @return 销售退货单列表
     */
    List<SaleReturnsSheetPO> findAllByState(SaleReturnsSheetState state);

    /**
     * 根据 saleReturnsSheetId 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId 销售退货单id
     * @param state 销售退货单状态
     * @return 影响的条目数
     */
    int updateState(String saleReturnsSheetId, SaleReturnsSheetState state);

    /**
     * 根据 saleReturnsSheetId 和 prevState 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId 销售退货单id
     * @param prevState 销售退货单之前的状态
     * @param state 销售退货单状态
     * @return 影响的条目数
     */
    int updateStateV2(String saleReturnsSheetId, SaleReturnsSheetState prevState, SaleReturnsSheetState state);

    /**
     * 通过saleReturnsSheetId找到对应条目
     * @param saleReturnsSheetId 销售退货单id
     * @return
     */
    SaleReturnsSheetPO findOneById(String saleReturnsSheetId);

    /**
     * 通过saleReturnsSheetId找到对应的content条目
     * @param saleReturnsSheetId
     * @return
     */
    List<SaleReturnsSheetContentPO> findContentBySaleReturnsSheetId(String saleReturnsSheetId);

}
