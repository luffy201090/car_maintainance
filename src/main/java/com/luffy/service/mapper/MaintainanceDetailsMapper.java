package com.luffy.service.mapper;

import com.luffy.domain.Maintainance;
import com.luffy.domain.MaintainanceDetails;
import com.luffy.domain.User;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.dto.MaintainanceDetailsDTO;
import com.luffy.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaintainanceDetails} and its DTO {@link MaintainanceDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintainanceDetailsMapper extends EntityMapper<MaintainanceDetailsDTO, MaintainanceDetails> {
    @Mapping(target = "maintainance", source = "maintainance", qualifiedByName = "maintainanceLevel")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    MaintainanceDetailsDTO toDto(MaintainanceDetails s);

    @Named("maintainanceLevel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "level", source = "level")
    MaintainanceDTO toDtoMaintainanceLevel(Maintainance maintainance);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
