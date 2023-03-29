package com.luffy.service.impl;

import com.luffy.domain.MaintainanceDetails;
import com.luffy.repository.MaintainanceDetailsRepository;
import com.luffy.service.MaintainanceDetailsService;
import com.luffy.service.dto.MaintainanceDetailsDTO;
import com.luffy.service.mapper.MaintainanceDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MaintainanceDetails}.
 */
@Service
@Transactional
public class MaintainanceDetailsServiceImpl implements MaintainanceDetailsService {

    private final Logger log = LoggerFactory.getLogger(MaintainanceDetailsServiceImpl.class);

    private final MaintainanceDetailsRepository maintainanceDetailsRepository;

    private final MaintainanceDetailsMapper maintainanceDetailsMapper;

    public MaintainanceDetailsServiceImpl(
        MaintainanceDetailsRepository maintainanceDetailsRepository,
        MaintainanceDetailsMapper maintainanceDetailsMapper
    ) {
        this.maintainanceDetailsRepository = maintainanceDetailsRepository;
        this.maintainanceDetailsMapper = maintainanceDetailsMapper;
    }

    @Override
    public MaintainanceDetailsDTO save(MaintainanceDetailsDTO maintainanceDetailsDTO) {
        log.debug("Request to save MaintainanceDetails : {}", maintainanceDetailsDTO);
        MaintainanceDetails maintainanceDetails = maintainanceDetailsMapper.toEntity(maintainanceDetailsDTO);
        maintainanceDetails = maintainanceDetailsRepository.save(maintainanceDetails);
        return maintainanceDetailsMapper.toDto(maintainanceDetails);
    }

    @Override
    public MaintainanceDetailsDTO update(MaintainanceDetailsDTO maintainanceDetailsDTO) {
        log.debug("Request to update MaintainanceDetails : {}", maintainanceDetailsDTO);
        MaintainanceDetails maintainanceDetails = maintainanceDetailsMapper.toEntity(maintainanceDetailsDTO);
        maintainanceDetails = maintainanceDetailsRepository.save(maintainanceDetails);
        return maintainanceDetailsMapper.toDto(maintainanceDetails);
    }

    @Override
    public Optional<MaintainanceDetailsDTO> partialUpdate(MaintainanceDetailsDTO maintainanceDetailsDTO) {
        log.debug("Request to partially update MaintainanceDetails : {}", maintainanceDetailsDTO);

        return maintainanceDetailsRepository
            .findById(maintainanceDetailsDTO.getId())
            .map(existingMaintainanceDetails -> {
                maintainanceDetailsMapper.partialUpdate(existingMaintainanceDetails, maintainanceDetailsDTO);

                return existingMaintainanceDetails;
            })
            .map(maintainanceDetailsRepository::save)
            .map(maintainanceDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintainanceDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MaintainanceDetails");
        return maintainanceDetailsRepository.findAll(pageable).map(maintainanceDetailsMapper::toDto);
    }

    public Page<MaintainanceDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return maintainanceDetailsRepository.findAllWithEagerRelationships(pageable).map(maintainanceDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintainanceDetailsDTO> findOne(Long id) {
        log.debug("Request to get MaintainanceDetails : {}", id);
        return maintainanceDetailsRepository.findOneWithEagerRelationships(id).map(maintainanceDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaintainanceDetails : {}", id);
        maintainanceDetailsRepository.deleteById(id);
    }
}
