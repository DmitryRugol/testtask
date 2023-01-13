package by.dmitryrugol.testtask.service;

import by.dmitryrugol.testtask.entity.Account;
import by.dmitryrugol.testtask.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    public static final int TRANSFER_INCORRECT_TRANSFER_AMOUNT = 1;
    public static final int TRANSFER_SOURCE_ACCOUNT_NOT_FOUND = 2;
    public static final int TRANSFER_NOT_ENOUGH_MONEY = 3;
    public static final int TRANSFER_DESTINATION_ACCOUNT_NOT_FOUND = 4;


    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int transfer(Long srcUsrId, Long dstUsrId, BigDecimal amount) {
        if (!(amount.compareTo(new BigDecimal(0)) == 1)) {
            return TRANSFER_INCORRECT_TRANSFER_AMOUNT;
        }

        Optional<Account> srcAccOpt = accountRepository.findAccountByUserId(srcUsrId);
        if (srcAccOpt.isEmpty()) {
            return TRANSFER_SOURCE_ACCOUNT_NOT_FOUND;
        }
        Account srcAcc = srcAccOpt.get();
        if (srcAcc.getBalance().compareTo(amount) == -1) {
            return TRANSFER_NOT_ENOUGH_MONEY;
        }
        Optional<Account> dstAccOpt = accountRepository.findAccountByUserId(dstUsrId);
        if (dstAccOpt.isEmpty()) {
            return TRANSFER_DESTINATION_ACCOUNT_NOT_FOUND;
        }
        Account dstAcc = dstAccOpt.get();
        srcAcc.setBalance(srcAcc.getBalance().subtract(amount));
        dstAcc.setBalance(dstAcc.getBalance().add(amount));
        return 0;
    }
}
