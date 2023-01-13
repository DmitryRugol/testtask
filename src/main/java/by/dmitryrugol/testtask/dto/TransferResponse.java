package by.dmitryrugol.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TransferResponse implements Serializable {
    int status;
    String description;
}
