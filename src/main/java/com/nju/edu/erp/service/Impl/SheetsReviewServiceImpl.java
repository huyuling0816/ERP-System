package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.vo.operationSheet.Expenditure;
import com.nju.edu.erp.model.vo.operationSheet.IncomeAfterDiscount;
import com.nju.edu.erp.model.vo.OperationSheetVO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseCountingVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategy;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByPricePacks;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByTotalPrice;
import com.nju.edu.erp.service.strategy.promotionStrategy.PromotionStrategyByUserLevel;
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
public class SheetsReviewServiceImpl implements SheetsReviewService {

    private final SaleService saleService;
    private final SaleReturnsService saleReturnsService;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final PurchaseReturnsService purchaseReturnsService;
    private final PurchaseService purchaseService;
    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final UserService userService;
    private final GiftSheetService giftSheetService;
    private final CustomerService customerService;
    private final PromotionStrategyByUserLevel promotionStrategyByUserLevel;
    private final PromotionStrategyByPricePacks promotionStrategyByPricePacks;
    private final PromotionStrategyByTotalPrice promotionStrategyByTotalPrice;

    @Autowired
    public SheetsReviewServiceImpl(SaleService saleService,
                                   SaleReturnsService saleReturnsService,
                                   ProductService productService,
                                   WarehouseService warehouseService,
                                   PurchaseReturnsService purchaseReturnsService,
                                   PurchaseService purchaseService,
                                   PaymentService paymentService,
                                   ReceiptService receiptService,
                                   UserService userService, GiftSheetService giftSheetService, CustomerService customerService, PromotionStrategyByUserLevel promotionStrategyByUserLevel, PromotionStrategyByPricePacks promotionStrategyByPricePacks, PromotionStrategyByTotalPrice promotionStrategyByTotalPrice) {

        this.saleService = saleService;
        this.saleReturnsService = saleReturnsService;
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
        this.userService = userService;
        this.giftSheetService = giftSheetService;
        this.customerService = customerService;
        this.promotionStrategyByUserLevel = promotionStrategyByUserLevel;
        this.promotionStrategyByPricePacks = promotionStrategyByPricePacks;
        this.promotionStrategyByTotalPrice = promotionStrategyByTotalPrice;
    }

    /**
     * 查看销售明细表：设定一个时间段，查看此时间段内的销售/销售退货的时间/商品名/型号/数量/单价/总额
     *
     * @param beginDateStr 开始时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @param endDateStr   结束时间字符串  格式为："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByTime(String beginDateStr, String endDateStr) {
        return saleService.getSaleDetailByTime(beginDateStr, endDateStr);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByProduct(String productName) {
        return saleService.getSaleDetailByProduct(productName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByCustomer(String customerName) {
        return saleService.getSaleDetailByCustomer(customerName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByOperator(String operatorName) {
        return saleService.getSaleDetailByOperator(operatorName);
    }

    @Transactional
    @Override
    public List<SaleDetailPO> getSaleDetailByWarehouse(String warehouseId) {
        return saleService.getSaleDetailByWarehouse(warehouseId);
    }

    /**
     * 销售收入、商品类收入（商品报溢收入,成本调价收入,进货退货差价,代金券与实际收款差额收入）
     *
     * @return 折让后的总收入
     */
    @Transactional
    @Override
    public IncomeAfterDiscount getIncomeAfterDiscount() {
        BigDecimal res = BigDecimal.ZERO;

        // 销售收入
        BigDecimal saleIncome = BigDecimal.ZERO;
        List<SaleSheetVO> saleSheetVOList = saleService.getSaleSheetByState(SaleSheetState.SUCCESS);
        for (SaleSheetVO saleSheetVO : saleSheetVOList) {
            saleIncome = saleIncome.add(saleSheetVO.getFinalAmount());
            res = res.add(saleSheetVO.getFinalAmount());
        }

        // 商品报溢收入
        BigDecimal commodityOverflowIncome = BigDecimal.ZERO;
        List<ProductInfoVO> productInfoVOList = productService.queryAllProduct();
        List<WarehouseCountingVO> warehouseCountingVOList = warehouseService.warehouseCounting();
        for (WarehouseCountingVO warehouseCountingVO : warehouseCountingVOList) {
            Integer actualQuantity = warehouseCountingVO.getQuantity();
            ProductInfoVO product = warehouseCountingVO.getProduct();
            int difference = product.getQuantity() - actualQuantity;
            if (difference > 0) { // 实际库房中的商品数量比系统中的多
                if (product.getRecentPp() == null) {
                    res = res.add(product.getPurchasePrice().multiply(BigDecimal.valueOf(difference)));
                    commodityOverflowIncome = commodityOverflowIncome.add(product.getPurchasePrice().multiply(BigDecimal.valueOf(difference)));
                } else {
                    res = res.add(product.getRecentPp().multiply(BigDecimal.valueOf(difference)));
                    commodityOverflowIncome = commodityOverflowIncome.add(product.getRecentPp().multiply(BigDecimal.valueOf(difference)));
                }
            }
        }

        // 成本调价收入
        BigDecimal costAdjustmentIncome = BigDecimal.ZERO;
        for (ProductInfoVO productInfoVO : productInfoVOList) {
            if (productInfoVO.getRecentPp() == null) {
                continue;
            }
            BigDecimal difference = productInfoVO.getPurchasePrice().subtract(productInfoVO.getRecentPp());
            res = res.add(difference.multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
            costAdjustmentIncome = costAdjustmentIncome.add(difference.multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
        }

        // 进货退货差价;
        BigDecimal saleReturnDiffIncome = BigDecimal.ZERO;
        List<PurchaseReturnsSheetVO> purchaseReturnsSheetList = purchaseReturnsService.getPurchaseReturnsSheetByState(PurchaseReturnsSheetState.SUCCESS);
        for (PurchaseReturnsSheetVO purchaseReturnsSheetVO : purchaseReturnsSheetList) {
            String purchaseSheetId = purchaseReturnsSheetVO.getPurchaseSheetId();
            PurchaseSheetVO purchaseSheet = purchaseService.getPurchaseSheetById(purchaseSheetId);
            BigDecimal difference = purchaseReturnsSheetVO.getTotalAmount().subtract(purchaseSheet.getTotalAmount());
            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                res = res.add(difference);
                saleReturnDiffIncome = saleReturnDiffIncome.add(difference);
            }
        }

        // 代金券与实际收款差额收入
        BigDecimal voucherDiffIncome = BigDecimal.ZERO;
        for (SaleSheetVO saleSheetVO : saleSheetVOList) {
            BigDecimal subtract = saleSheetVO.getVoucherAmount().subtract(saleSheetVO.getFinalAmount());
            if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                res = res.add(subtract);
                voucherDiffIncome = voucherDiffIncome.add(subtract);
            }
        }

        IncomeAfterDiscount incomeAfterDiscount = IncomeAfterDiscount.builder()
                .saleIncome(saleIncome)
                .commodityOverflowIncome(commodityOverflowIncome)
                .costAdjustmentIncome(costAdjustmentIncome)
                .saleReturnDiffIncome(saleReturnDiffIncome)
                .voucherDiffIncome(voucherDiffIncome)
                .totalIncome(res)
                .build();
        return incomeAfterDiscount;
    }

    @Transactional
    @Override
    public BigDecimal getTotalIncomeDiscount() {
        BigDecimal res = BigDecimal.ZERO;
        List<SaleSheetVO> saleSheetVOList = saleService.getSaleSheetByState(SaleSheetState.SUCCESS);
        for (SaleSheetVO saleSheetVO : saleSheetVOList) {
            BigDecimal subtract = saleSheetVO.getRawTotalAmount().subtract(saleSheetVO.getFinalAmount());
            if (subtract.compareTo(BigDecimal.ZERO) > 0)
                res = res.add(subtract);
        }
        return res;
    }

    @Transactional
    @Override
    public void redFlush(SheetVO sheetVO) {
        String id = sheetVO.getId();
        if (id.startsWith("XSD")) {
            if (saleService.getSaleSheetById(id).getState() != SaleSheetState.SUCCESS) {
                saleService.getSaleSheetById(id).setState(SaleSheetState.FAILURE);
                return;
            }
            String operator = saleService.getSaleSheetById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            SaleSheetVO saleSheetVO = saleService.getSaleSheetById(id);

            // 各数值取负，抵消之前的单据
            saleSheetVO.setRawTotalAmount(saleSheetVO.getRawTotalAmount().multiply(BigDecimal.valueOf(-1)));
            saleSheetVO.setFinalAmount(saleSheetVO.getFinalAmount().multiply(BigDecimal.valueOf(-1)));
            saleSheetVO.setVoucherAmount(saleSheetVO.getVoucherAmount().multiply(BigDecimal.valueOf(-1)));
            saleSheetVO.getSaleSheetContent().forEach(saleSheetContentVO -> {
                saleSheetContentVO.setQuantity(saleSheetContentVO.getQuantity() * -1);
                saleSheetContentVO.setTotalPrice(saleSheetContentVO.getTotalPrice().multiply(BigDecimal.valueOf(-1)));
            });

            List<PromotionStrategy> promotionStrategyList = new ArrayList<>();
            promotionStrategyList.add(promotionStrategyByTotalPrice);
            promotionStrategyList.add(promotionStrategyByPricePacks);
            promotionStrategyList.add(promotionStrategyByUserLevel);
            saleService.makeSaleSheet(userVO, saleSheetVO, promotionStrategyList);
        } else if (id.startsWith("XSTHD")) {
            if (saleReturnsService.getSaleReturnsSheetById(id).getState() != SaleReturnsSheetState.SUCCESS) {
                saleReturnsService.getSaleReturnsSheetById(id).setState(SaleReturnsSheetState.FAILURE);
                return;
            }
            String operator = saleReturnsService.getSaleReturnsSheetById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            SaleReturnsSheetVO saleReturnsSheetVO = saleReturnsService.getSaleReturnsSheetById(id);

            // 各数值取负，抵消之前的单据
            saleReturnsSheetVO.setRawTotalAmount(saleReturnsSheetVO.getRawTotalAmount().multiply(BigDecimal.valueOf(-1)));
            saleReturnsSheetVO.setFinalAmount(saleReturnsSheetVO.getFinalAmount().multiply(BigDecimal.valueOf(-1)));
            saleReturnsSheetVO.setVoucherAmount(saleReturnsSheetVO.getVoucherAmount().multiply(BigDecimal.valueOf(-1)));
            saleReturnsSheetVO.getSaleReturnsSheetContent().forEach(saleReturnsSheetContentVO -> {
                saleReturnsSheetContentVO.setQuantity(saleReturnsSheetContentVO.getQuantity() * -1);
                saleReturnsSheetContentVO.setTotalPrice(saleReturnsSheetContentVO.getTotalPrice().multiply(BigDecimal.valueOf(-1)));
            });

            saleReturnsService.makeSaleReturnsSheet(userVO, saleReturnsSheetVO);
        } else if (id.startsWith("JHD")) {
            if (purchaseService.getPurchaseSheetById(id).getState() != PurchaseSheetState.SUCCESS) {
                purchaseService.getPurchaseSheetById(id).setState(PurchaseSheetState.FAILURE);
                return;
            }
            String operator = purchaseService.getPurchaseSheetById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            PurchaseSheetVO purchaseSheetVO = purchaseService.getPurchaseSheetById(id);

            // 各数值取负，抵消之前的单据
            purchaseSheetVO.setTotalAmount(purchaseSheetVO.getTotalAmount().multiply(BigDecimal.valueOf(-1)));
            purchaseSheetVO.getPurchaseSheetContent().forEach(purchaseSheetContentVO -> {
                purchaseSheetContentVO.setQuantity(purchaseSheetContentVO.getQuantity() * -1);
                purchaseSheetContentVO.setTotalPrice(purchaseSheetContentVO.getTotalPrice().multiply(BigDecimal.valueOf(-1)));
            });

            purchaseService.makePurchaseSheet(userVO, purchaseSheetVO);
        } else if (id.startsWith("JHTHD")) {
            if (purchaseReturnsService.getPurchaseReturnsById(id).getState() != PurchaseReturnsSheetState.SUCCESS) {
                purchaseReturnsService.getPurchaseReturnsById(id).setState(PurchaseReturnsSheetState.FAILURE);
                return;
            }
            String operator = purchaseReturnsService.getPurchaseReturnsById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            PurchaseReturnsSheetVO purchaseReturnsSheetVO = purchaseReturnsService.getPurchaseReturnsById(id);

            // 各数值取负，抵消之前的单据
            purchaseReturnsSheetVO.setTotalAmount(purchaseReturnsSheetVO.getTotalAmount().multiply(BigDecimal.valueOf(-1)));
            purchaseReturnsSheetVO.getPurchaseReturnsSheetContent().forEach(purchaseReturnsSheetContentVO -> {
                purchaseReturnsSheetContentVO.setQuantity(purchaseReturnsSheetContentVO.getQuantity() * -1);
                purchaseReturnsSheetContentVO.setTotalPrice(purchaseReturnsSheetContentVO.getTotalPrice().multiply(BigDecimal.valueOf(-1)));
            });

            purchaseReturnsService.makePurchaseReturnsSheet(userVO, purchaseReturnsSheetVO);
        } else if (id.startsWith("FKD")) {
            if (paymentService.getPaymentSheetById(id).getState() != FinanceSheetState.SUCCESS) {
                paymentService.getPaymentSheetById(id).setState(FinanceSheetState.FAILURE);
                return;
            }
            String operator = paymentService.getPaymentSheetById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            PaymentSheetVO paymentSheetVO = paymentService.getPaymentSheetById(id);

            // 各数值取负，抵消之前的单据
            paymentSheetVO.setTotalAmount(paymentSheetVO.getTotalAmount().multiply(BigDecimal.valueOf(-1)));
            paymentSheetVO.getPaymentSheetContent().forEach(paymentSheetContentVO -> {
                paymentSheetContentVO.setTransferAmount(paymentSheetContentVO.getTransferAmount().multiply(BigDecimal.valueOf(-1)));
            });

            paymentService.makePaymentSheet(userVO, paymentSheetVO);
        } else if (id.startsWith("SKD")) {
            if (receiptService.getReceiptSheetById(id).getState() != FinanceSheetState.SUCCESS) {
                receiptService.getReceiptSheetById(id).setState(FinanceSheetState.FAILURE);
                return;
            }
            String operator = receiptService.getReceiptSheetById(id).getOperator();
            UserVO userVO = userService.findUserByName(operator);
            ReceiptSheetVO receiptSheetVO = receiptService.getReceiptSheetById(id);

            // 各数值取负，抵消之前的单据
            receiptSheetVO.setTotalAmount(receiptSheetVO.getTotalAmount().multiply(BigDecimal.valueOf(-1)));
            receiptSheetVO.getReceiptSheetContent().forEach(receiptSheetContentVO -> {
                receiptSheetContentVO.setTransferAmount(receiptSheetContentVO.getTransferAmount().multiply(BigDecimal.valueOf(-1)));
            });

            receiptService.makeReceiptSheet(userVO, receiptSheetVO);
        }
    }

    @Transactional
    @Override
    public SheetVO redFlushAndCopy(SheetVO sheetVO) {
        redFlush(sheetVO);
        return sheetVO;
    }


    /**
     * 支出类：销售成本、商品类支出（商品报损,商品赠出）、人力成本。支出类显示总支出。
     *
     * @return
     */
    @Transactional
    @Override
    public Expenditure getTotalExpenditure() {
        BigDecimal res = BigDecimal.ZERO;

        // 销售成本，即进价
        BigDecimal purchasePrice = BigDecimal.ZERO;
        List<ProductInfoVO> productInfoVOList = productService.queryAllProduct();
        for (ProductInfoVO productInfoVO : productInfoVOList) {
            if (productInfoVO.getRecentPp() == null) {
                res = res.add(productInfoVO.getPurchasePrice().multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
                purchasePrice = purchasePrice.add(productInfoVO.getPurchasePrice().multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
            } else {
                res = res.add(productInfoVO.getRecentPp().multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
                purchasePrice = purchasePrice.add(productInfoVO.getRecentPp().multiply(BigDecimal.valueOf(productInfoVO.getQuantity())));
            }
        }

        // 商品报损
        BigDecimal commodityLoss = BigDecimal.ZERO;
        List<WarehouseCountingVO> warehouseCountingVOList = warehouseService.warehouseCounting();
        for (WarehouseCountingVO warehouseCountingVO : warehouseCountingVOList) {
            Integer actualQuantity = warehouseCountingVO.getQuantity();
            ProductInfoVO product = warehouseCountingVO.getProduct();
            int difference = product.getQuantity() - actualQuantity;
            if (difference < 0) { // 实际库房中的商品数量比系统中的少
                if (product.getRecentPp() == null) {
                    res = res.add(product.getPurchasePrice().multiply(BigDecimal.valueOf(difference)));
                    commodityLoss = commodityLoss.add(product.getPurchasePrice().multiply(BigDecimal.valueOf(difference)));
                } else {
                    res = res.add(product.getRecentPp().multiply(BigDecimal.valueOf(difference)));
                    commodityLoss = commodityLoss.add(product.getRecentPp().multiply(BigDecimal.valueOf(difference)));
                }
            }
        }

        // 商品赠送
        BigDecimal giftAmount = BigDecimal.ZERO;
        List<GiftSheetPO> giftSheetPOS = giftSheetService.findAll();
        for (GiftSheetPO giftSheetPO : giftSheetPOS) {
            res = res.add(giftSheetPO.getTotalPrice());
            giftAmount = giftAmount.add(giftSheetPO.getTotalPrice());
        }

        // 人力成本
        BigDecimal humanResourceAmount = BigDecimal.ZERO;

        Expenditure expenditure = Expenditure.builder()
                .purchasePrice(purchasePrice)
                .commodityLoss(commodityLoss)
                .giftAmount(giftAmount)
                .humanResourceAmount(humanResourceAmount)
                .totalExpenditure(res)
                .build();

        return expenditure;
    }

    @Transactional
    @Override
    public BigDecimal getTotalProfit() {
        return getIncomeAfterDiscount().getTotalIncome().subtract(getTotalExpenditure().getTotalExpenditure());
    }

    @Transactional
    @Override
    public OperationSheetVO getOperationSheet() {
        OperationSheetVO operationSheetVO = new OperationSheetVO();
        operationSheetVO.setIncomeAfterDiscount(getIncomeAfterDiscount().getTotalIncome());
        operationSheetVO.setTotalIncomeDiscount(getTotalIncomeDiscount());
        operationSheetVO.setTotalExpenditure(getTotalExpenditure().getTotalExpenditure());
        operationSheetVO.setTotalProfit(getTotalProfit());
        return operationSheetVO;
    }

    @Transactional
    @Override
    public List<SheetVO> getSheetByTime(String beginDateStr, String endDateStr) {
        List<SheetVO> res = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginDate = format.parse(beginDateStr);
            Date endDate = format.parse(endDateStr);
            if (beginDate.compareTo(endDate) > 0) {
                throw new RuntimeException("Error！开始时间大于结束时间！");
            } else {
                List<SaleSheetPO> saleSheetList = saleService.findAllByCreateTime(beginDate, endDate);
                List<SaleReturnsSheetPO> saleReturnSheetList = saleReturnsService.findAllByCreateTime(beginDate, endDate);
                List<PurchaseSheetPO> purchaseSheetList = purchaseService.findAllByCreateTime(beginDate, endDate);
                List<PurchaseReturnsSheetPO> purchaseReturnsSheetList = purchaseReturnsService.findAllByCreateTime(beginDate, endDate);
                List<PaymentSheetPO> paymentSheetList = paymentService.findAllByCreateTime(beginDate, endDate);
                List<ReceiptSheetPO> receiptSheetList = receiptService.findAllByCreateTime(beginDate, endDate);

                saleSheetList.forEach(saleSheetPO -> {
                    res.add(saleService.getSaleSheetById(saleSheetPO.getId()));
                });
                saleReturnSheetList.forEach(saleReturnsSheetPO -> {
                    res.add(saleReturnsService.getSaleReturnsSheetById(saleReturnsSheetPO.getId()));
                });
                purchaseSheetList.forEach(purchaseSheetPO -> {
                    res.add(purchaseService.getPurchaseSheetById(purchaseSheetPO.getId()));
                });
                purchaseReturnsSheetList.forEach(purchaseReturnsSheetPO -> {
                    res.add(purchaseReturnsService.getPurchaseReturnsById(purchaseReturnsSheetPO.getId()));
                });
                paymentSheetList.forEach(paymentSheetPO -> {
                    res.add(paymentService.getPaymentSheetById(paymentSheetPO.getId()));
                });
                receiptSheetList.forEach(receiptSheetPO -> {
                    res.add(receiptService.getReceiptSheetById(receiptSheetPO.getId()));
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Transactional
    @Override
    public List<SheetVO> getSheetBySheetType(String sheetType) {
        if (sheetType.length() == 0) {
            return null;
        }
        List<SheetVO> res = new ArrayList<>();
        switch (sheetType) {
            case "销售出货单":
                List<SaleSheetPO> saleSheetList = saleService.findAllSheet();
                saleSheetList.forEach(saleSheetPO -> {
                    res.add(saleService.getSaleSheetById(saleSheetPO.getId()));
                });
                break;
            case "销售退货单":
                List<SaleReturnsSheetPO> saleReturnSheetList = saleReturnsService.findAll();
                saleReturnSheetList.forEach(saleReturnsSheetPO -> {
                    res.add(saleReturnsService.getSaleReturnsSheetById(saleReturnsSheetPO.getId()));
                });
                break;
            case "进货单":
                List<PurchaseSheetPO> purchaseSheetList = purchaseService.findAll();
                purchaseSheetList.forEach(purchaseSheetPO -> {
                    res.add(purchaseService.getPurchaseSheetById(purchaseSheetPO.getId()));
                });
                break;
            case "进货退货单":
                List<PurchaseReturnsSheetPO> purchaseReturnsSheetList = purchaseReturnsService.findAll();
                purchaseReturnsSheetList.forEach(purchaseReturnsSheetPO -> {
                    res.add(purchaseReturnsService.getPurchaseReturnsById(purchaseReturnsSheetPO.getId()));
                });
                break;
            case "付款单":
                List<PaymentSheetPO> paymentSheetList = paymentService.findAllSheet();
                paymentSheetList.forEach(paymentSheetPO -> {
                    res.add(paymentService.getPaymentSheetById(paymentSheetPO.getId()));
                });
                break;
            case "收款单":
                List<ReceiptSheetPO> receiptSheetList = receiptService.findAllSheet();
                receiptSheetList.forEach(receiptSheetPO -> {
                    res.add(receiptService.getReceiptSheetById(receiptSheetPO.getId()));
                });
                break;
        }
        return res;
    }

    @Transactional
    @Override
    public List<SheetVO> getSheetByCustomerName(String customerName) {
        if (customerName.length() == 0) {
            return null;
        }
        CustomerPO customerPO = customerService.findOneByName(customerName);
        Integer customerId = customerPO.getId();
        List<SheetVO> res = new ArrayList<>();

        List<SaleSheetPO> saleSheetList = saleService.findAllByCustomerId(customerId);
        List<SaleReturnsSheetPO> saleReturnsSheetList = saleReturnsService.findAllByCustomerId(customerId);
        List<PurchaseSheetPO> purchaseSheetList = purchaseService.findAllByCustomerId(customerId);
        List<PurchaseReturnsSheetPO> purchaseReturnsSheetList = purchaseReturnsService.findAllByCustomerId(customerId);
        List<PaymentSheetPO> paymentSheetList = paymentService.findAllByCustomerId(customerId);
        List<ReceiptSheetPO> receiptSheetList = receiptService.findAllByCustomerId(customerId);

        saleSheetList.forEach(saleSheetPO -> {
            res.add(saleService.getSaleSheetById(saleSheetPO.getId()));
        });

        saleReturnsSheetList.forEach(saleReturnsSheetPO -> {
            res.add(saleReturnsService.getSaleReturnsSheetById(saleReturnsSheetPO.getId()));
        });
        purchaseSheetList.forEach(purchaseSheetPO -> {
            res.add(purchaseService.getPurchaseSheetById(purchaseSheetPO.getId()));
        });
        purchaseReturnsSheetList.forEach(purchaseReturnsSheetPO -> {
            res.add(purchaseReturnsService.getPurchaseReturnsById(purchaseReturnsSheetPO.getId()));
        });
        paymentSheetList.forEach(paymentSheetPO -> {
            res.add(paymentService.getPaymentSheetById(paymentSheetPO.getId()));
        });
        receiptSheetList.forEach(receiptSheetPO -> {
            res.add(receiptService.getReceiptSheetById(receiptSheetPO.getId()));
        });
        return res;
    }

    @Transactional
    @Override
    public List<SheetVO> getSheetByOperatorName(String operatorName) {
        if (operatorName.length() == 0) {
            return null;
        }
        List<SheetVO> res = new ArrayList<>();

        List<SaleSheetPO> saleSheetList = saleService.findAllByOperatorName(operatorName);
        List<SaleReturnsSheetPO> saleReturnsSheetList = saleReturnsService.findAllByOperatorName(operatorName);
        List<PurchaseSheetPO> purchaseSheetList = purchaseService.findAllByOperatorName(operatorName);
        List<PurchaseReturnsSheetPO> purchaseReturnsSheetList = purchaseReturnsService.findAllByOperatorName(operatorName);
        List<PaymentSheetPO> paymentSheetList = paymentService.findAllByOperatorName(operatorName);
        List<ReceiptSheetPO> receiptSheetList = receiptService.findAllByOperatorName(operatorName);

        saleSheetList.forEach(saleSheetPO -> {
            res.add(saleService.getSaleSheetById(saleSheetPO.getId()));
        });

        saleReturnsSheetList.forEach(saleReturnsSheetPO -> {
            res.add(saleReturnsService.getSaleReturnsSheetById(saleReturnsSheetPO.getId()));
        });
        purchaseSheetList.forEach(purchaseSheetPO -> {
            res.add(purchaseService.getPurchaseSheetById(purchaseSheetPO.getId()));
        });
        purchaseReturnsSheetList.forEach(purchaseReturnsSheetPO -> {
            res.add(purchaseReturnsService.getPurchaseReturnsById(purchaseReturnsSheetPO.getId()));
        });
        paymentSheetList.forEach(paymentSheetPO -> {
            res.add(paymentService.getPaymentSheetById(paymentSheetPO.getId()));
        });
        receiptSheetList.forEach(receiptSheetPO -> {
            res.add(receiptService.getReceiptSheetById(receiptSheetPO.getId()));
        });
        return res;
    }


}
