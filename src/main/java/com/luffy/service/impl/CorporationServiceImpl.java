package com.luffy.service.impl;

import com.luffy.domain.Corporation;
import com.luffy.repository.CorporationRepository;
import com.luffy.service.CorporationService;
import com.luffy.service.dto.CorporationDTO;
import com.luffy.service.mapper.CorporationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Corporation}.
 */
@Service
@Transactional
public class CorporationServiceImpl implements CorporationService {

    private final Logger log = LoggerFactory.getLogger(CorporationServiceImpl.class);

    private final CorporationRepository corporationRepository;

    private final CorporationMapper corporationMapper;

    public CorporationServiceImpl(CorporationRepository corporationRepository, CorporationMapper corporationMapper) {
        this.corporationRepository = corporationRepository;
        this.corporationMapper = corporationMapper;
    }

    @Override
    public CorporationDTO save(CorporationDTO corporationDTO) {
        log.debug("Request to save Corporation : {}", corporationDTO);
        Corporation corporation = corporationMapper.toEntity(corporationDTO);
        corporation = corporationRepository.save(corporation);
        return corporationMapper.toDto(corporation);
    }

    @Override
    public CorporationDTO update(CorporationDTO corporationDTO) {
        log.debug("Request to update Corporation : {}", corporationDTO);
        Corporation corporation = corporationMapper.toEntity(corporationDTO);
        corporation = corporationRepository.save(corporation);
        return corporationMapper.toDto(corporation);
    }

    @Override
    public Optional<CorporationDTO> partialUpdate(CorporationDTO corporationDTO) {
        log.debug("Request to partially update Corporation : {}", corporationDTO);

        return corporationRepository
            .findById(corporationDTO.getId())
            .map(existingCorporation -> {
                corporationMapper.partialUpdate(existingCorporation, corporationDTO);

                return existingCorporation;
            })
            .map(corporationRepository::save)
            .map(corporationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CorporationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Corporations");
        return corporationRepository.findAll(pageable).map(corporationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CorporationDTO> findOne(Long id) {
        log.debug("Request to get Corporation : {}", id);
        return corporationRepository.findById(id).map(corporationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Corporation : {}", id);
        corporationRepository.deleteById(id);
    }
}
