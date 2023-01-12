package com.nju.edu.erp.service.strategy.approvalStrategy;

import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.FinanceSheetState;
import org.springframework.stereotype.Service;

@Service
public interface ApprovalStrategy {

    /**
     * 审批单据
     * @param sheetId
     * @param state
     */
    void approval(String sheetId, BaseEnum state);
}
