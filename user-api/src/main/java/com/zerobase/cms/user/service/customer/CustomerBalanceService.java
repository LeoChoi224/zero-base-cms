package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.cms.user.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;


@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(
            Long customerId,
            ChangeBalanceForm form
    ) throws CustomException {
        CustomerBalanceHistory customerBalanceHistory =
                customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                        .orElse(CustomerBalanceHistory.builder()
                                .chagneMoney(0)
                                .currentMoney(0)
                                .customer(customerRepository.findById(customerId)
                                        .orElseThrow(() -> new CustomException(NOT_FOUND_USER)))
                                .build());
        if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
            throw new CustomException(NOT_ENOUGH_BALANCE);
        }

        customerBalanceHistory = CustomerBalanceHistory.builder()
                .chagneMoney(form.getMoney())
                .currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
                .description(form.getMessage())
                .fromMessage(form.getFrom())
                .customer(customerBalanceHistory.getCustomer())
                .build();
        customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

        return customerBalanceHistoryRepository.save(customerBalanceHistory);
    }
}
