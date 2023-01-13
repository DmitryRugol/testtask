package by.dmitryrugol.testtask.rest;

import by.dmitryrugol.testtask.dto.SearchUsersFilterDto;
import by.dmitryrugol.testtask.dto.TransferRequestDto;
import by.dmitryrugol.testtask.dto.TransferResponseDto;
import by.dmitryrugol.testtask.dto.UserDto;
import by.dmitryrugol.testtask.jwt.config.JwtTokenUtil;
import by.dmitryrugol.testtask.service.AccountService;
import by.dmitryrugol.testtask.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static by.dmitryrugol.testtask.service.AccountService.*;

@RestController
@CrossOrigin
public class MainController {

    private final Logger log
            = LogManager.getLogger(this.getClass());

    private final AccountService accountService;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    public MainController(AccountService accountService,
                          UserService userService,
                          JwtTokenUtil jwtTokenUtil) {
        this.accountService = accountService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<TransferResponseDto> transfer(@RequestBody TransferRequestDto transferRequestDto,
                                                        HttpServletRequest request) {

        Objects.requireNonNull(transferRequestDto.getDstUserId());
        Objects.requireNonNull(transferRequestDto.getAmount());

        final String requestTokenHeader = request.getHeader("Authorization");

        Long srcUsrId;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                srcUsrId = jwtTokenUtil.getUserIdFromToken(jwtToken);
            } catch (Exception e) {
                log.error("Unable to find \"user_id\" claim in token");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            log.error("Incorrect token");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            int res = accountService.transfer(srcUsrId, transferRequestDto.getDstUserId(), transferRequestDto.getAmount());
            String description = null;

            // TODO: correct implementation
            switch (res) {
                case TRANSFER_INCORRECT_TRANSFER_AMOUNT:
                    description = "Incorrect transfer amount";
                    break;
                case TRANSFER_SOURCE_ACCOUNT_NOT_FOUND:
                    description = "Source account not found";
                    break;
                case TRANSFER_NOT_ENOUGH_MONEY:
                    description = "Not enough money";
                    break;
                case TRANSFER_DESTINATION_ACCOUNT_NOT_FOUND: // TODO: implementation
                    description = "Destination account not found";
                    break;
            }
            if (description != null) {
                log.warn(description);
                TransferResponseDto resp = new TransferResponseDto(res, description);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        } catch (Exception e) {
            TransferResponseDto resp = new TransferResponseDto(100, "Money transfer error");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        return new ResponseEntity<>(new TransferResponseDto(0, "Money transfer completed successfully"), HttpStatus.OK);
    }

    @GetMapping("users/search")
    public ResponseEntity<?> search(@RequestBody SearchUsersFilterDto searchFilter,
                                      HttpServletRequest request) {
        List<UserDto> res = userService.findByFilter(searchFilter);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
