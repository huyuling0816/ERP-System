package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.SaleReturnsSheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public interface SaleReturnsService {
    /**
     * 制定销售退货单
     * @param saleReturnsSheetVO 销售退货单
     */
    void makeSaleReturnsSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO);

    /**
     * 根据状态获取销售退货单(state == null 则获取所有销售退货单)
     * @param state 销售退货单状态
     * @return 销售退货单
     */
    List<SaleReturnsSheetVO> getSaleReturnsSheetByState(SaleReturnsSheetState state);

    /**
     * 根据销售退货单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     * @param saleReturnsSheetId 销售退货单id
     * @param state 销售退货单修改后的状态
     */
    void approval(String saleReturnsSheetId, SaleReturnsSheetState state);

    SaleReturnsSheetVO getSaleReturnsSheetById(String saleReturnsSheetId);

    List<SaleReturnsSheetPO> findAllByCreateTime(Date beginTime, Date endTime);

    List<SaleReturnsSheetPO> findAll();

    List<SaleReturnsSheetPO> findAllByCustomerId(Integer id);

    List<SaleReturnsSheetPO> findAllByOperatorName(String operator);
}
