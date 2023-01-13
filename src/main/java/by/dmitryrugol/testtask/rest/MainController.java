package by.dmitryrugol.testtask.rest;

import by.dmitryrugol.testtask.dto.TransferRequest;
import by.dmitryrugol.testtask.dto.TransferResponse;
import by.dmitryrugol.testtask.jwt.config.JwtTokenUtil;
import by.dmitryrugol.testtask.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static by.dmitryrugol.testtask.service.AccountService.*;

@RestController
@CrossOrigin
public class MainController {

    private final Logger log
            = LogManager.getLogger(this.getClass());

    @Autowired
    AccountService accountService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest,
                                                     HttpServletRequest request) {

        Objects.requireNonNull(transferRequest.getDstUserId());
        Objects.requireNonNull(transferRequest.getAmount());

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
            int res = accountService.transfer(srcUsrId, transferRequest.getDstUserId(), transferRequest.getAmount());
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
                TransferResponse resp = new TransferResponse(res, description);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
        } catch (Exception e) {
            TransferResponse resp = new TransferResponse(100, "Money transfer error");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        return new ResponseEntity<>(new TransferResponse(0, "Money transfer completed successfully"), HttpStatus.OK);
    }
}
