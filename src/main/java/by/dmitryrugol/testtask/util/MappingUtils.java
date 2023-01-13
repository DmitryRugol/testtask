package by.dmitryrugol.testtask.util;

import by.dmitryrugol.testtask.dto.UserDto;
import by.dmitryrugol.testtask.entity.User;
import org.springframework.stereotype.Service;

@Service
public class MappingUtils {

    public UserDto entityToUserDto(User entity){
        UserDto dto = new UserDto(entity.getId(),
                entity.getName(),
                entity.getDateOfBirth());
        return dto;
    }
}
