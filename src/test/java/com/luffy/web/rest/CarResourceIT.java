package com.luffy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luffy.IntegrationTest;
import com.luffy.domain.Brand;
import com.luffy.domain.Car;
import com.luffy.domain.User;
import com.luffy.repository.CarRepository;
import com.luffy.service.CarService;
import com.luffy.service.criteria.CarCriteria;
import com.luffy.service.dto.CarDTO;
import com.luffy.service.mapper.CarMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Mock
    private CarRepository carRepositoryMock;

    @Autowired
    private CarMapper carMapper;

    @Mock
    private CarService carServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car().name(DEFAULT_NAME).model(DEFAULT_MODEL).plate(DEFAULT_PLATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        car.setUser(user);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        car.setBrand(brand);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car().name(UPDATED_NAME).model(UPDATED_MODEL).plate(UPDATED_PLATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        car.setUser(user);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createUpdatedEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        car.setBrand(brand);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        restCarMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getPlate()).isEqualTo(DEFAULT_PLATE);
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setName(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setPlate(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        restCarMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE));
    }

    @Test
    @Transactional
    void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name equals to DEFAULT_NAME
        defaultCarShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the carList where name equals to UPDATED_NAME
        defaultCarShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCarShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the carList where name equals to UPDATED_NAME
        defaultCarShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name is not null
        defaultCarShouldBeFound("name.specified=true");

        // Get all the carList where name is null
        defaultCarShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByNameContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name contains DEFAULT_NAME
        defaultCarShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the carList where name contains UPDATED_NAME
        defaultCarShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where name does not contain DEFAULT_NAME
        defaultCarShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the carList where name does not contain UPDATED_NAME
        defaultCarShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model equals to DEFAULT_MODEL
        defaultCarShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the carList where model equals to UPDATED_MODEL
        defaultCarShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultCarShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the carList where model equals to UPDATED_MODEL
        defaultCarShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model is not null
        defaultCarShouldBeFound("model.specified=true");

        // Get all the carList where model is null
        defaultCarShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByModelContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model contains DEFAULT_MODEL
        defaultCarShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the carList where model contains UPDATED_MODEL
        defaultCarShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where model does not contain DEFAULT_MODEL
        defaultCarShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the carList where model does not contain UPDATED_MODEL
        defaultCarShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarsByPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where plate equals to DEFAULT_PLATE
        defaultCarShouldBeFound("plate.equals=" + DEFAULT_PLATE);

        // Get all the carList where plate equals to UPDATED_PLATE
        defaultCarShouldNotBeFound("plate.equals=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByPlateIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where plate in DEFAULT_PLATE or UPDATED_PLATE
        defaultCarShouldBeFound("plate.in=" + DEFAULT_PLATE + "," + UPDATED_PLATE);

        // Get all the carList where plate equals to UPDATED_PLATE
        defaultCarShouldNotBeFound("plate.in=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where plate is not null
        defaultCarShouldBeFound("plate.specified=true");

        // Get all the carList where plate is null
        defaultCarShouldNotBeFound("plate.specified=false");
    }

    @Test
    @Transactional
    void getAllCarsByPlateContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where plate contains DEFAULT_PLATE
        defaultCarShouldBeFound("plate.contains=" + DEFAULT_PLATE);

        // Get all the carList where plate contains UPDATED_PLATE
        defaultCarShouldNotBeFound("plate.contains=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByPlateNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where plate does not contain DEFAULT_PLATE
        defaultCarShouldNotBeFound("plate.doesNotContain=" + DEFAULT_PLATE);

        // Get all the carList where plate does not contain UPDATED_PLATE
        defaultCarShouldBeFound("plate.doesNotContain=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllCarsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            carRepository.saveAndFlush(car);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        car.setUser(user);
        carRepository.saveAndFlush(car);
        String userId = user.getId();

        // Get all the carList where user equals to userId
        defaultCarShouldBeFound("userId.equals=" + userId);

        // Get all the carList where user equals to "invalid-id"
        defaultCarShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllCarsByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            carRepository.saveAndFlush(car);
            brand = BrandResourceIT.createEntity(em);
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        car.setBrand(brand);
        carRepository.saveAndFlush(car);
        Long brandId = brand.getId();

        // Get all the carList where brand equals to brandId
        defaultCarShouldBeFound("brandId.equals=" + brandId);

        // Get all the carList where brand equals to (brandId + 1)
        defaultCarShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)));

        // Check, that the count call also returns 1
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar.name(UPDATED_NAME).model(UPDATED_MODEL).plate(UPDATED_PLATE);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getPlate()).isEqualTo(UPDATED_PLATE);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.name(UPDATED_NAME).model(UPDATED_MODEL).plate(UPDATED_PLATE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getPlate()).isEqualTo(UPDATED_PLATE);
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.name(UPDATED_NAME).model(UPDATED_MODEL).plate(UPDATED_PLATE);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getPlate()).isEqualTo(UPDATED_PLATE);
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc
            .perform(delete(ENTITY_API_URL_ID, car.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
