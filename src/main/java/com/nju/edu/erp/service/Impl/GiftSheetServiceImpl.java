package com.nju.edu.erp.service.Impl;
import com.nju.edu.erp.dao.GiftSheetDao;
import com.nju.edu.erp.enums.sheetState.GiftSheetState;
import com.nju.edu.erp.model.po.GiftSheetPO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.promotionStrategy.GiftSheetVO;
import com.nju.edu.erp.service.GiftSheetService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftSheetServiceImpl implements GiftSheetService {

    private final GiftSheetDao giftSheetDao;

    private final ProductService productService;
    @Autowired
    public GiftSheetServiceImpl(GiftSheetDao giftSheetDao, ProductService productService){
        this.giftSheetDao = giftSheetDao;
        this.productService = productService;
    }

    @Transactional
    @Override
    public void makeGiftSheet(GiftSheetPO giftSheetPO) {
        GiftSheetPO latest = giftSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(),"ZSD");
        giftSheetPO.setId(id);
        giftSheetPO.setState(GiftSheetState.PENDING);
        ProductInfoVO productInfoVO = productService.getOneProductByPid(giftSheetPO.getPid());
        giftSheetPO.setUnitPrice(productInfoVO.getRetailPrice());
        giftSheetPO.setTotalPrice(BigDecimal.valueOf(giftSheetPO.getNumber()).multiply(giftSheetPO.getUnitPrice()));
        giftSheetDao.saveSheet(giftSheetPO);
    }

    @Transactional
    @Override
    public List<GiftSheetVO> getGiftSheetByState(GiftSheetState state) {
        List<GiftSheetVO> res = new ArrayList<>();
        List<GiftSheetPO> all;
        if(state == null){
            all = giftSheetDao.findAll();
        } else {
            all = giftSheetDao.findAllByState(state);
        }
        for(GiftSheetPO po : all){
            GiftSheetVO vo = new GiftSheetVO();
            BeanUtils.copyProperties(po,vo);
            res.add(vo);
        }
        return res;
    }

    @Transactional
    @Override
    public List<GiftSheetPO> findAll() {
        return giftSheetDao.findAll();
    }
}
