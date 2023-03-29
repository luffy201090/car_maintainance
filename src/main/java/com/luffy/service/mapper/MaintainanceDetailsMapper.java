package com.luffy.service.mapper;

import com.luffy.domain.Maintainance;
import com.luffy.domain.MaintainanceDetails;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.dto.MaintainanceDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaintainanceDetails} and its DTO {@link MaintainanceDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintainanceDetailsMapper extends EntityMapper<MaintainanceDetailsDTO, MaintainanceDetails> {
    @Mapping(target = "maintainance", source = "maintainance", qualifiedByName = "maintainanceLevel")
    MaintainanceDetailsDTO toDto(MaintainanceDetails s);

    @Named("maintainanceLevel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "level", source = "level")
    MaintainanceDTO toDtoMaintainanceLevel(Maintainance maintainance);
}
