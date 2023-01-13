package by.dmitryrugol.testtask.rest;

import by.dmitryrugol.testtask.entity.Account;
import by.dmitryrugol.testtask.repository.AccountRepository;
import by.dmitryrugol.testtask.repository.UserRepository;
import by.dmitryrugol.testtask.service.AccountService;
import by.dmitryrugol.testtask.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;


@RestController
public class HomeController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    private final Logger log
            = LogManager.getLogger(this.getClass());

    @GetMapping("/")
    public @ResponseBody
    String greeting() {

//        Optional<User> uo = userRepository.findById(1L);
//
//        User u = uo.get();
//        u.getEmails().add(new EmailData(u, "someemai2"));
//        u.getEmails().remove(0);
//        u.getPhones().add(new PhoneData(u, "12345678901"));
//        u.getPhones().remove(0);
//        userRepository.save(u);
//        Optional<User> uf = userRepository.findUserByPhones_phone("12345678901");
//        List<User> uf1 = userRepository.findUserByNameLikeOrPhones_phone_OrEmails_email("n234", "12345678901", "em9");
//        Optional<User> uf = userRepository.findByPasswordAndEmailsOrPhones("123123123", "12345678901");


        Optional<Account> a = accountRepository.findAccountByUserId(10L);



//        User user = new User("test1", new Date(), "test1111");
//        user.setId(9L);
//        user = userRepository.save(user);

        int res = accountService.transfer(3L, 1L, new BigDecimal(5.25));
//        userService.addAccountToUser(9L, new BigDecimal(99));
//        Account account = new Account(user, new BigDecimal(99));
//        accountRepository.save(account);

        log.debug("Hello, World");
        log.info("Hello, World");
        log.warn("Hello, World");
        log.error("Hello, World");
        log.fatal("Hello, World");
        return "Hello, World";
    }

}
