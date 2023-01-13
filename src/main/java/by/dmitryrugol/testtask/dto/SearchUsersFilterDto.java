package by.dmitryrugol.testtask.dto;

import lombok.Data;

@Data
public class SearchUsersFilterDto {
    private String name;
    private String dateOfBirth;
    private String email;
    private String phone;

    private int page;
    private int pageSize;
}
