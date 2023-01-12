package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PromotionStrategyByPricePacksDao;
import com.nju.edu.erp.dao.PromotionStrategyByTotalPriceDao;
import com.nju.edu.erp.dao.PromotionStrategyByUserLevelDao;
import com.nju.edu.erp.model.po.PromotionStrategyByPricePacksPO;
import com.nju.edu.erp.model.po.PromotionStrategyByTotalPricePO;
import com.nju.edu.erp.model.po.PromotionStrategyByUserLevelPO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByPricePacksVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByTotalPriceVO;
import com.nju.edu.erp.model.vo.promotionStrategy.PromotionStrategyByUserLevelVO;
import com.nju.edu.erp.service.PromotionService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionStrategyByUserLevelDao promotionStrategyByUserLevelDao;

    private final PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao;

    private final PromotionStrategyByTotalPriceDao promotionStrategyByTotalPriceDao;

    @Autowired
    public PromotionServiceImpl(PromotionStrategyByUserLevelDao promotionStrategyByUserLevelDao, PromotionStrategyByPricePacksDao promotionStrategyByPricePacksDao, PromotionStrategyByTotalPriceDao promotionStrategyByTotalPriceDao) {
        this.promotionStrategyByUserLevelDao = promotionStrategyByUserLevelDao;
        this.promotionStrategyByPricePacksDao = promotionStrategyByPricePacksDao;
        this.promotionStrategyByTotalPriceDao = promotionStrategyByTotalPriceDao;
    }

    @Override
    public void makePromotionStrategyByUserLevel(List<PromotionStrategyByUserLevelVO> list) {
        List<PromotionStrategyByUserLevelPO> pos = new ArrayList<>();
        PromotionStrategyByUserLevelPO latest = promotionStrategyByUserLevelDao.getLatestStrategy();
        String sid = IdGenerator.generateSheetId(latest == null ? null : latest.getSid(), "CXCLL");
        for (PromotionStrategyByUserLevelVO promotionStrategyByUserLevelVO : list) {
            PromotionStrategyByUserLevelPO po = new PromotionStrategyByUserLevelPO();
            BeanUtils.copyProperties(promotionStrategyByUserLevelVO, po);
            po.setSid(sid);
            pos.add(po);
        }
        for (PromotionStrategyByUserLevelPO promotionStrategyByUserLevelPO : pos) {
            promotionStrategyByUserLevelDao.saveStrategy(promotionStrategyByUserLevelPO);
        }
    }

    @Override
    public void makePromotionStrategyByPricePacks(PromotionStrategyByPricePacksVO promotionStrategyByPricePacks) {
        PromotionStrategyByPricePacksPO promotionStrategyByPricePacksPO = new PromotionStrategyByPricePacksPO();
        PromotionStrategyByPricePacksPO latest = promotionStrategyByPricePacksDao.getLatestSheet();
        String sid = IdGenerator.generateSheetId(latest == null ? null : latest.getSid(), "CXCLP");
        BeanUtils.copyProperties(promotionStrategyByPricePacks, promotionStrategyByPricePacksPO);
        promotionStrategyByPricePacksPO.setSid(sid);
        promotionStrategyByPricePacksDao.saveStrategy(promotionStrategyByPricePacksPO);
    }

    @Override
    public void makePromotionStrategyByTotalPrice(List<PromotionStrategyByTotalPriceVO> list) {
        List<PromotionStrategyByTotalPricePO> pos = new ArrayList<>();
        PromotionStrategyByTotalPricePO latest = promotionStrategyByTotalPriceDao.getLatestStrategy();
        String sid = IdGenerator.generateSheetId(latest == null ? null : latest.getSid(), "CXCLT");
        for (PromotionStrategyByTotalPriceVO promotionStrategyByTotalPriceVO : list) {
            PromotionStrategyByTotalPricePO po = new PromotionStrategyByTotalPricePO();
            BeanUtils.copyProperties(promotionStrategyByTotalPriceVO, po);
            po.setSid(sid);
            pos.add(po);
        }
        for (PromotionStrategyByTotalPricePO po : pos) {
            promotionStrategyByTotalPriceDao.saveStrategy(po);
        }
    }

    @Override
    public List<PromotionStrategyByUserLevelVO> findAllPromotionStrategyByUserLevel() {
        List<PromotionStrategyByUserLevelPO> pos = promotionStrategyByUserLevelDao.findAll();
        List<PromotionStrategyByUserLevelVO> res = new ArrayList<>();
        for (PromotionStrategyByUserLevelPO po : pos) {
            PromotionStrategyByUserLevelVO vo = new PromotionStrategyByUserLevelVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<PromotionStrategyByPricePacksVO> findAllPromotionStrategyByPricePacks() {
        List<PromotionStrategyByPricePacksPO> pos = promotionStrategyByPricePacksDao.findAll();
        List<PromotionStrategyByPricePacksVO> res = new ArrayList<>();
        for (PromotionStrategyByPricePacksPO po : pos) {
            PromotionStrategyByPricePacksVO vo = new PromotionStrategyByPricePacksVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<PromotionStrategyByTotalPriceVO> findAllPromotionStrategyByTotalPrice() {
        List<PromotionStrategyByTotalPricePO> pos = promotionStrategyByTotalPriceDao.findAll();
        List<PromotionStrategyByTotalPriceVO> res = new ArrayList<>();
        for (PromotionStrategyByTotalPricePO po : pos) {
            PromotionStrategyByTotalPriceVO vo = new PromotionStrategyByTotalPriceVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }


}
