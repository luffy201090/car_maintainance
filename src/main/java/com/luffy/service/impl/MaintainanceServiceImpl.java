package com.luffy.service.impl;

import com.luffy.domain.Maintainance;
import com.luffy.repository.MaintainanceRepository;
import com.luffy.service.MaintainanceService;
import com.luffy.service.UserService;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.mapper.MaintainanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Maintainance}.
 */
@Service
@Transactional
public class MaintainanceServiceImpl implements MaintainanceService {

    private final Logger log = LoggerFactory.getLogger(MaintainanceServiceImpl.class);

    private final MaintainanceRepository maintainanceRepository;

    private final MaintainanceMapper maintainanceMapper;

    private final UserService userService;

    public MaintainanceServiceImpl(MaintainanceRepository maintainanceRepository, MaintainanceMapper maintainanceMapper, UserService userService) {
        this.maintainanceRepository = maintainanceRepository;
        this.maintainanceMapper = maintainanceMapper;
        this.userService = userService;
    }

    @Override
    public MaintainanceDTO save(MaintainanceDTO maintainanceDTO) {
        log.debug("Request to save Maintainance : {}", maintainanceDTO);
        Maintainance maintainance = maintainanceMapper.toEntity(maintainanceDTO);
        maintainance.setUser(userService.getCurrentUser());
        maintainance = maintainanceRepository.save(maintainance);
        return maintainanceMapper.toDto(maintainance);
    }

    @Override
    public MaintainanceDTO update(MaintainanceDTO maintainanceDTO) {
        log.debug("Request to update Maintainance : {}", maintainanceDTO);
        Maintainance maintainance = maintainanceMapper.toEntity(maintainanceDTO);
        maintainance = maintainanceRepository.save(maintainance);
        return maintainanceMapper.toDto(maintainance);
    }

    @Override
    public Optional<MaintainanceDTO> partialUpdate(MaintainanceDTO maintainanceDTO) {
        log.debug("Request to partially update Maintainance : {}", maintainanceDTO);

        return maintainanceRepository
            .findById(maintainanceDTO.getId())
            .map(existingMaintainance -> {
                maintainanceMapper.partialUpdate(existingMaintainance, maintainanceDTO);

                return existingMaintainance;
            })
            .map(maintainanceRepository::save)
            .map(maintainanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintainanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Maintainances");
        return maintainanceRepository.findAll(pageable).map(maintainanceMapper::toDto);
    }

    public Page<MaintainanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return maintainanceRepository.findAllWithEagerRelationships(pageable).map(maintainanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintainanceDTO> findOne(Long id) {
        log.debug("Request to get Maintainance : {}", id);
        return maintainanceRepository.findOneWithEagerRelationships(id).map(maintainanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Maintainance : {}", id);
        maintainanceRepository.deleteById(id);
    }
}
