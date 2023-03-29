package com.luffy.service.impl;

import com.luffy.domain.Car;
import com.luffy.repository.CarRepository;
import com.luffy.service.CarService;
import com.luffy.service.dto.CarDTO;
import com.luffy.service.mapper.CarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Override
    public CarDTO save(CarDTO carDTO) {
        log.debug("Request to save Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public CarDTO update(CarDTO carDTO) {
        log.debug("Request to update Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    @Override
    public Optional<CarDTO> partialUpdate(CarDTO carDTO) {
        log.debug("Request to partially update Car : {}", carDTO);

        return carRepository
            .findById(carDTO.getId())
            .map(existingCar -> {
                carMapper.partialUpdate(existingCar, carDTO);

                return existingCar;
            })
            .map(carRepository::save)
            .map(carMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAll(pageable).map(carMapper::toDto);
    }

    public Page<CarDTO> findAllWithEagerRelationships(Pageable pageable) {
        return carRepository.findAllWithEagerRelationships(pageable).map(carMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarDTO> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findOneWithEagerRelationships(id).map(carMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
