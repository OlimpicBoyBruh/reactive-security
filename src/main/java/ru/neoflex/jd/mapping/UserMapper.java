package ru.neoflex.jd.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.neoflex.jd.dto.UserDto;
import ru.neoflex.jd.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    @InheritInverseConfiguration
    User toEntity(UserDto userDto);

}
