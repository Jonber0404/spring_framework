package com.yrgo.services.calls;

import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
@Service("callHandlingService")
public class CallHandlingServiceImpl implements CallHandlingService{

    @Autowired
    private CustomerManagementService customerManagementService;

    @Autowired
    private DiaryManagementService diaryManagementService;

    /*public CallHandlingServiceImpl(CustomerManagementService customerManagementService, DiaryManagementService diaryManagementService) {
        this.customerManagementService = customerManagementService;
        this.diaryManagementService = diaryManagementService;
    }*/

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException, RecordNotFoundException {
        customerManagementService.recordCall(customerId, newCall);

        for (Action action: actions) {
            diaryManagementService.recordAction(action);
        }

    }
}
