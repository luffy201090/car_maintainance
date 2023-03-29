package com.luffy.service.impl;

import com.luffy.domain.Brand;
import com.luffy.repository.BrandRepository;
import com.luffy.service.BrandService;
import com.luffy.service.dto.BrandDTO;
import com.luffy.service.mapper.BrandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Brand}.
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public BrandDTO save(BrandDTO brandDTO) {
        log.debug("Request to save Brand : {}", brandDTO);
        Brand brand = brandMapper.toEntity(brandDTO);
        brand = brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    @Override
    public BrandDTO update(BrandDTO brandDTO) {
        log.debug("Request to update Brand : {}", brandDTO);
        Brand brand = brandMapper.toEntity(brandDTO);
        brand = brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    @Override
    public Optional<BrandDTO> partialUpdate(BrandDTO brandDTO) {
        log.debug("Request to partially update Brand : {}", brandDTO);

        return brandRepository
            .findById(brandDTO.getId())
            .map(existingBrand -> {
                brandMapper.partialUpdate(existingBrand, brandDTO);

                return existingBrand;
            })
            .map(brandRepository::save)
            .map(brandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Brands");
        return brandRepository.findAll(pageable).map(brandMapper::toDto);
    }

    public Page<BrandDTO> findAllWithEagerRelationships(Pageable pageable) {
        return brandRepository.findAllWithEagerRelationships(pageable).map(brandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BrandDTO> findOne(Long id) {
        log.debug("Request to get Brand : {}", id);
        return brandRepository.findOneWithEagerRelationships(id).map(brandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Brand : {}", id);
        brandRepository.deleteById(id);
    }
}
