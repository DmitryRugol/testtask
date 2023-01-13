package by.dmitryrugol.testtask.service;

import by.dmitryrugol.testtask.entity.Account;
import by.dmitryrugol.testtask.entity.User;
import by.dmitryrugol.testtask.repository.AccountRepository;
import by.dmitryrugol.testtask.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    AccountRepository accountRepository;

    UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User getByEmailOrPhone(String searchStr) {
        return userRepository.findByEmailOrPhone(searchStr).orElse(null);
    }

    @Transactional
    public void addAccountToUser(Long usrId, BigDecimal balance) {
        Optional<User> user = userRepository.findById(usrId);
        if (user.isPresent()) {
            Account account = new Account(user.get(), balance);
            accountRepository.save(account);
        }
    }

}
