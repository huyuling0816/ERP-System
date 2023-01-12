package com.nju.edu.erp.service.Impl;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleReturnsSheetDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.dao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SaleReturnsServiceImpl implements SaleReturnsService {

    SaleReturnsSheetDao saleReturnsSheetDao;

    SaleService saleService;

    ProductService productService;

    ProductDao productDao;

    SaleSheetDao saleSheetDao;

    CustomerService customerService;

    WarehouseService warehouseService;

    WarehouseDao warehouseDao;

    @Autowired
    public SaleReturnsServiceImpl(SaleReturnsSheetDao saleReturnsSheetDao,SaleService saleService,ProductDao productDao,SaleSheetDao saleSheetDao,CustomerService customerService,WarehouseService warehouseService,WarehouseDao warehouseDao,ProductService productService){
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.saleService = saleService;
        this.productDao = productDao;
        this.saleSheetDao = saleSheetDao;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.warehouseDao = warehouseDao;
        this.productService = productService;
    }

    @Override
    @Transactional
    public void makeSaleReturnsSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO) {
        BigDecimal temp1 = saleReturnsSheetVO.getRawTotalAmount();
        BigDecimal temp2 = saleReturnsSheetVO.getVoucherAmount();
        SaleReturnsSheetPO saleReturnsSheetPO = new SaleReturnsSheetPO();
        BeanUtils.copyProperties(saleReturnsSheetVO,saleReturnsSheetPO);
        saleReturnsSheetPO.setOperator(userVO.getName());
        saleReturnsSheetPO.setCreateTime(new Date());
        SaleReturnsSheetPO latest = saleReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId( latest == null ? null :latest.getId(),"XSTHD");
        saleReturnsSheetPO.setId(id);
        saleReturnsSheetPO.setState(SaleReturnsSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleReturnsSheetPO.getSaleSheetId());
        Map<String, SaleSheetContentPO> map = new HashMap<>();
        for(SaleSheetContentPO item : saleSheetContent){
            map.put(item.getPid(), item);
        }
        List<SaleReturnsSheetContentPO> sContentPOlist = new ArrayList<>();
        for(SaleReturnsSheetContentVO content : saleReturnsSheetVO.getSaleReturnsSheetContent()){
            SaleReturnsSheetContentPO sContentPO = new SaleReturnsSheetContentPO();
            BeanUtils.copyProperties(content,sContentPO);
            sContentPO.setSaleReturnsSheetId(id);
            SaleSheetContentPO item = map.get(sContentPO.getPid());
            sContentPO.setUnitPrice(item.getUnitPrice());
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOlist.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }
        saleReturnsSheetDao.saveBatch(sContentPOlist);
        // 计算退货商品中的代金券总额
        saleReturnsSheetPO.setRawTotalAmount(totalAmount);
        saleReturnsSheetPO.setFinalAmount(totalAmount.multiply(saleReturnsSheetPO.getDiscount()).subtract(saleReturnsSheetPO.getVoucherAmount().multiply(saleReturnsSheetPO.getRawTotalAmount().divide(temp1))));
        saleReturnsSheetPO.setVoucherAmount(temp2.multiply(saleReturnsSheetPO.getRawTotalAmount()).divide(temp1));
        saleReturnsSheetDao.save(saleReturnsSheetPO);
    }

    @Override
    @Transactional
    public List<SaleReturnsSheetVO> getSaleReturnsSheetByState(SaleReturnsSheetState state) {
        List<SaleReturnsSheetVO> res = new ArrayList<>();
        List<SaleReturnsSheetPO> all;
        if (state == null){
            all = saleReturnsSheetDao.findAll();
        } else {
            all = saleReturnsSheetDao.findAllByState(state);
        }
        for(SaleReturnsSheetPO po : all){
            SaleReturnsSheetVO vo = new SaleReturnsSheetVO();
            BeanUtils.copyProperties(po,vo);
            List<SaleReturnsSheetContentPO> alll = saleReturnsSheetDao.findContentBySaleReturnsSheetId(po.getId());
            List<SaleReturnsSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnsSheetContentPO p : alll){
                SaleReturnsSheetContentVO v = new SaleReturnsSheetContentVO();
                BeanUtils.copyProperties(p,v);
                vos.add(v);
            }
            vo.setSaleReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    @Transactional
    public void approval(String saleReturnsSheetId, SaleReturnsSheetState state) {
        SaleReturnsSheetPO saleReturnsSheet = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if(state.equals(SaleReturnsSheetState.FAILURE)){
            if(saleReturnsSheet.getState() == SaleReturnsSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleReturnsSheetDao.updateState(saleReturnsSheetId, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleReturnsSheetState prevState;
            if(state.equals(SaleReturnsSheetState.SUCCESS)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_2;
            } else if(state.equals(SaleReturnsSheetState.PENDING_LEVEL_2)){
                prevState = SaleReturnsSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleReturnsSheetDao.updateStateV2(saleReturnsSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("状态更新失败");
            if(state.equals(SaleReturnsSheetState.SUCCESS)){
                // TODO
                List<SaleReturnsSheetContentPO> contents = saleReturnsSheetDao.findContentBySaleReturnsSheetId(saleReturnsSheetId);
                for(SaleReturnsSheetContentPO content : contents){
                    String pid = content.getPid();
                    Integer quantity = content.getQuantity();

                    int batchId = 0;
                    WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid, batchId);
                    while (warehousePO==null && batchId < 100){
                        warehousePO = warehouseDao.findOneByPidAndBatchId(pid, batchId);
                        batchId ++;
                    }
                    if(warehousePO==null) throw new RuntimeException("单据发生错误！请联系管理员！");
                    warehousePO.setQuantity(quantity);
                    warehouseDao.addQuantity(warehousePO);
                    ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                    productInfoVO.setQuantity(productInfoVO.getQuantity()+quantity);
                    productService.updateProduct(productInfoVO);
                }
                SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnsSheet.getSaleSheetId());
                Integer supplier = saleSheetPO.getSupplier();
                CustomerPO customer = customerService.findCustomerById(supplier);
                customer.setReceivable(customer.getReceivable().add(saleReturnsSheet.getFinalAmount()));
                customerService.updateCustomer(customer);
            }
        }
    }

    @Transactional
    @Override
    public SaleReturnsSheetVO getSaleReturnsSheetById(String saleReturnsSheetId) {
        SaleReturnsSheetPO saleReturnsSheetPO = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if(saleReturnsSheetPO == null) return null;
        List<SaleReturnsSheetContentPO> contentPO = saleReturnsSheetDao.findContentBySaleReturnsSheetId(saleReturnsSheetId);
        SaleReturnsSheetVO sVO = new SaleReturnsSheetVO();
        BeanUtils.copyProperties(saleReturnsSheetPO, sVO);
        List<SaleReturnsSheetContentVO> saleReturnsSheetContentVOList = new ArrayList<>();
        for (SaleReturnsSheetContentPO content:
                contentPO) {
            SaleReturnsSheetContentVO sContentVO = new SaleReturnsSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleReturnsSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleReturnsSheetContent(saleReturnsSheetContentVOList);
        return sVO;
    }

    @Transactional
    @Override
    public List<SaleReturnsSheetPO> findAllByCreateTime(Date beginTime, Date endTime) {
        return saleReturnsSheetDao.findAllByCreateTime(beginTime, endTime);
    }

    @Transactional
    @Override
    public List<SaleReturnsSheetPO> findAll() {
        return saleReturnsSheetDao.findAll();
    }

    @Transactional
    @Override
    public List<SaleReturnsSheetPO> findAllByCustomerId(Integer id){return saleReturnsSheetDao.findAllByCustomerId(id);}

    @Transactional
    @Override
    public List<SaleReturnsSheetPO> findAllByOperatorName(String operator){
        return saleReturnsSheetDao.findAllByOperatorName(operator);
    }
}
