package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategy;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByPricePacks;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByTotalPrice;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByUserLevel;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;


    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO, List<PromotionStrategy> promotionStrategyList) {
        // TODO
        // ??????
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO, saleSheetPO);
        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for (SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()) {
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content, sContentPO);
            sContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            if (unitPrice == null) {
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getPurchasePrice();
                sContentPO.setUnitPrice(unitPrice);
            }
            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(sContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);

        // ?????????????????????
        promotionStrategyList.forEach(promotionStrategy -> {
            promotionStrategy.updateSaleSheet(saleSheetPO);
        });

        // ?????????????????????
        BigDecimal finalAmount = totalAmount.multiply(saleSheetPO.getDiscount()).subtract(saleSheetPO.getVoucherAmount());
        if (saleSheetPO.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = finalAmount.compareTo(BigDecimal.ZERO) >= 0 ? finalAmount : BigDecimal.ZERO;
        }
        saleSheetPO.setFinalAmount(finalAmount);
        saleSheetDao.saveSheet(saleSheetPO);
        // ???????????????????????????SaleSheet???????????????content???SaleSheetContent??????????????????????????????????????????????????????????????????
        // ?????????service???dao???????????????????????????????????????????????????????????????
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if (state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllByState(state);
        }
        for (SaleSheetPO po : all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : alll) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
        // ?????????????????????????????????????????????VO??????SaleSheetContent???
        // ?????????dao?????????????????????????????????????????????
    }

    /**
     * ???????????????id????????????(state == "???????????????"/"????????????"/"????????????")
     * ???controller?????????????????????
     *
     * @param saleSheetId ?????????id
     * @param state       ???????????????????????????
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // ?????????service???dao???????????????????????????????????????????????????????????????
        /* ??????????????????
            1. ????????????????????????????????????
                 1. ??????????????????
                 2. ???????????????
                 3. ???????????????
                 4. ??????????????????
            2. ?????????????????????????????????????????????????????? ????????????????????????????????????????????????
         */
        if (state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if (saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if (effectLines == 0) throw new RuntimeException("??????????????????");
        } else {
            SaleSheetState prevState;
            if (state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("??????????????????");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("??????????????????");
            if (state.equals(SaleSheetState.SUCCESS)) {
                // ???????????????
                List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS = new ArrayList<>();

                for (SaleSheetContentPO content : saleSheetContent) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());
                    productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
                    woContentVO.setSalePrice(content.getUnitPrice());
                    woContentVO.setQuantity(content.getQuantity());
                    woContentVO.setRemark(content.getRemark());
                    woContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(woContentVO);
                }
                // ???????????????(??????????????????)
                // ???????????? receivable
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                // ??????????????????
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null);
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????(?????????????????????,??????????????????????????????,????????????????????????????????????????????????)
     *
     * @param salesman     ?????????????????????
     * @param beginDateStr ?????????????????????
     * @param endDateStr   ?????????????????????
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman, String beginDateStr, String endDateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginTime = dateFormat.parse(beginDateStr);
            Date endTime = dateFormat.parse(endDateStr);
            if (beginTime.compareTo(endTime) > 0) {
                return null;
            } else {
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman, beginTime, endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ???????????????Id?????????????????????
     *
     * @param saleSheetId ?????????Id
     * @return ?????????????????????
     */
    @Transactional
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if (saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content :
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByTime(String beginDateStr, String endDateStr) {
        List<SaleDetailPO> result = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginDate = format.parse(beginDateStr);
            Date endDate = format.parse(endDateStr);
            if (beginDate.compareTo(endDate) > 0) {
                throw new RuntimeException("Error????????????????????????????????????");
            } else {
                List<SaleDetailPO> saleList = saleSheetDao.getSaleDetailByTime(beginDate, endDate);
                for (SaleDetailPO saleDetailPO : saleList) {
                    result.add(saleDetailPO);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByProduct(String productName) {
        if (productName.length() == 0) {
            return null;
        }
        return saleSheetDao.getSaleDetailByProduct(productName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByCustomer(String customerName) {
        if (customerName.length() == 0) {
            return null;
        }
        return saleSheetDao.getSaleDetailByCustomer(customerName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByOperator(String operatorName) {
        if (operatorName.length() == 0) {
            return null;
        }
        return saleSheetDao.getSaleDetailByOperator(operatorName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByWarehouse(String warehouseId) {
        if (warehouseId.length() == 0) {
            return null;
        }
        return saleSheetDao.getSaleDetailByWarehouse(warehouseId);
    }

    @Transactional
    @Override
    public List<SaleSheetPO> findAllByCreateTime(Date beginTime, Date endTime) {
        return saleSheetDao.findAllByCreateTime(beginTime, endTime);
    }

    @Transactional
    @Override
    public List<SaleSheetPO> findAllSheet() {
        return saleSheetDao.findAllSheet();
    }

    @Transactional
    @Override
    public List<SaleSheetPO> findAllByCustomerId(Integer id) {
        return saleSheetDao.findAllByCustomerId(id);
    }

    @Transactional
    @Override
    public List<SaleSheetPO> findAllByOperatorName(String operator){
        return saleSheetDao.findAllByOperatorName(operator);
    }
}
