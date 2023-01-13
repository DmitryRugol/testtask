package by.dmitryrugol.testtask.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TransferRequestDto implements Serializable {
    private Long dstUserId;
    private BigDecimal amount;
}
