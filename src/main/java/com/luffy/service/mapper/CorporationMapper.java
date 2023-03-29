package com.luffy.service.mapper;

import com.luffy.domain.Corporation;
import com.luffy.service.dto.CorporationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Corporation} and its DTO {@link CorporationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorporationMapper extends EntityMapper<CorporationDTO, Corporation> {}
