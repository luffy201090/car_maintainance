package com.luffy.service.mapper;

import com.luffy.domain.Car;
import com.luffy.domain.Maintainance;
import com.luffy.domain.User;
import com.luffy.service.dto.CarDTO;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maintainance} and its DTO {@link MaintainanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintainanceMapper extends EntityMapper<MaintainanceDTO, Maintainance> {
    @Mapping(target = "car", source = "car", qualifiedByName = "carName")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    MaintainanceDTO toDto(Maintainance s);

    @Named("carName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CarDTO toDtoCarName(Car car);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
