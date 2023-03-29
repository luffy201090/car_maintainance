package com.luffy.service.mapper;

import com.luffy.domain.Brand;
import com.luffy.domain.Car;
import com.luffy.domain.User;
import com.luffy.service.dto.BrandDTO;
import com.luffy.service.dto.CarDTO;
import com.luffy.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandName")
    CarDTO toDto(Car s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("brandName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BrandDTO toDtoBrandName(Brand brand);
}
