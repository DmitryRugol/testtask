package by.dmitryrugol.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TransferResponseDto implements Serializable {
    private int status;
    private String description;
}
