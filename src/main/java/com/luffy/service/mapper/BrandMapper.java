package com.luffy.service.mapper;

import com.luffy.domain.Brand;
import com.luffy.domain.Corporation;
import com.luffy.service.dto.BrandDTO;
import com.luffy.service.dto.CorporationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "corporation", source = "corporation", qualifiedByName = "corporationName")
    BrandDTO toDto(Brand s);

    @Named("corporationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CorporationDTO toDtoCorporationName(Corporation corporation);
}
