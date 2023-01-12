package com.nju.edu.erp.dao;
import java.util.List;
import com.nju.edu.erp.model.po.PromotionStrategyByUserLevelPO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByUserLevelVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PromotionStrategyByUserLevelDao {

    /**
     * 获取最近一条策略
     *
     * @return
     */
    PromotionStrategyByUserLevelPO getLatestStrategy();
    /**
     * 存入一条促销策略记录（只是五条中的一条）
     *
     * @param toSave 一条促销策略记录
     * @return 影响的行数
     */
    int saveStrategy(PromotionStrategyByUserLevelPO toSave);
    /**
     * 返回记录的条数
     *
     * @return 记录的条数
     */
    int numOfRecords();
    /**
     * 根据策略编号及用户级别找到相应的促销策略
     *
     * @param sid 策略编号
     * @param level 用户级别
     * @return 促销策略po
     */
    PromotionStrategyByUserLevelPO findStrategyBySidAndLevel(String sid,Integer level);

    List<PromotionStrategyByUserLevelPO> findAll();

}
