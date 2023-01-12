package com.nju.edu.erp.dao;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.GiftSheetPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GiftSheetDao {

    /**
     * 获取最近一条收款单
     * @return
     */
    GiftSheetPO getLatestSheet();

    /**
     * 存入一条赠品单记录
     * @param toSave 一条赠品单记录
     * @return 影响的行数
     */
    int saveSheet(GiftSheetPO toSave);
    /**
     * 根据状态查找收款单
     */
    List<GiftSheetPO> findAllByState(GiftSheetState state);

    List<GiftSheetPO> findAll();
    /**
     * 查找指定id的销售单
     *
     * @param id
     * @return
     */
    GiftSheetPO findSheetById(String id);
    /**
     * 更新指定销售单的状态
     *
     * @param sheetId
     * @param state
     * @return
     */
    int updateSheetState(String sheetId, GiftSheetState state);
    /**
     * 根据当前状态更新销售单状态
     *
     * @param sheetId
     * @param prev
     * @param state
     * @return
     */
    int updateSheetStateOnPrev(String sheetId, GiftSheetState prev, GiftSheetState state);

}
