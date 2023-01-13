package by.dmitryrugol.testtask.service;

import by.dmitryrugol.testtask.dto.SearchUsersFilterDto;
import by.dmitryrugol.testtask.dto.UserDto;
import by.dmitryrugol.testtask.entity.Account;
import by.dmitryrugol.testtask.entity.User;
import by.dmitryrugol.testtask.repository.AccountRepository;
import by.dmitryrugol.testtask.repository.UserRepository;
import by.dmitryrugol.testtask.util.MappingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger log
            = LogManager.getLogger(this.getClass());

    @PersistenceContext
    EntityManager em;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final MappingUtils mappingUtils;

    UserService(UserRepository userRepository,
                AccountRepository accountRepository,
                MappingUtils mappingUtils) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.mappingUtils = mappingUtils;
    }

    public User getByEmailOrPhone(String searchStr) {
        return userRepository.findByEmailOrPhone(searchStr).orElse(null);
    }

    public List<UserDto> findByFilter(SearchUsersFilterDto searchFilter) {

        // TODO: sql injection protection!!!
        String sql = "select u.* from users u where 1 = 1";
        if (searchFilter.getName() != null && !searchFilter.getName().isBlank()) {
            sql += " and u.name like '" + searchFilter.getName() + "%'";
        }

        if (searchFilter.getDateOfBirth() != null
                && !searchFilter.getDateOfBirth().isBlank()) {

            // check if the birthday is in correct format
            try {
                Date dateOfBirth = new SimpleDateFormat("dd.MM.yyyy")
                        .parse(searchFilter.getDateOfBirth());
            } catch (ParseException e) {
                log.error("Incorrect dateOfBirth in requested users filter");
                // TODO: check if need to interrupt
            }

            sql += " and u.date_of_birth >= to_date('"
                    + searchFilter.getDateOfBirth() + "', 'DD.MM.YYYY')";
        }

        sql += " order by u.id";

        if (searchFilter.getPageSize() > 0) {
            sql += " limit " + searchFilter.getPageSize()
                    + " offset " + searchFilter.getPageSize() * (searchFilter.getPage() - 1);
        }

        log.info("Executing sql for users search: " + sql);
        List<User> ul = em.createNativeQuery(sql, User.class).getResultList();

        log.info("Found " + ul.size() + " users");

        List<UserDto> res = ul.stream()
                .map(mappingUtils::entityToUserDto)
                .collect(Collectors.toList());
        return res;
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
