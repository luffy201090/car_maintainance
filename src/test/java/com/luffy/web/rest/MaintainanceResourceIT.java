package com.luffy.web.rest;

import static com.luffy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luffy.IntegrationTest;
import com.luffy.domain.Car;
import com.luffy.domain.Maintainance;
import com.luffy.domain.User;
import com.luffy.repository.MaintainanceRepository;
import com.luffy.service.MaintainanceService;
import com.luffy.service.criteria.MaintainanceCriteria;
import com.luffy.service.dto.MaintainanceDTO;
import com.luffy.service.mapper.MaintainanceMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MaintainanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MaintainanceResourceIT {

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/maintainances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaintainanceRepository maintainanceRepository;

    @Mock
    private MaintainanceRepository maintainanceRepositoryMock;

    @Autowired
    private MaintainanceMapper maintainanceMapper;

    @Mock
    private MaintainanceService maintainanceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintainanceMockMvc;

    private Maintainance maintainance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintainance createEntity(EntityManager em) {
        Maintainance maintainance = new Maintainance().level(DEFAULT_LEVEL).price(DEFAULT_PRICE).place(DEFAULT_PLACE).date(DEFAULT_DATE);
        // Add required entity
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            car = CarResourceIT.createEntity(em);
            em.persist(car);
            em.flush();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        maintainance.setCar(car);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        maintainance.setUser(user);
        return maintainance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintainance createUpdatedEntity(EntityManager em) {
        Maintainance maintainance = new Maintainance().level(UPDATED_LEVEL).price(UPDATED_PRICE).place(UPDATED_PLACE).date(UPDATED_DATE);
        // Add required entity
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            car = CarResourceIT.createUpdatedEntity(em);
            em.persist(car);
            em.flush();
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        maintainance.setCar(car);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        maintainance.setUser(user);
        return maintainance;
    }

    @BeforeEach
    public void initTest() {
        maintainance = createEntity(em);
    }

    @Test
    @Transactional
    void createMaintainance() throws Exception {
        int databaseSizeBeforeCreate = maintainanceRepository.findAll().size();
        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);
        restMaintainanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeCreate + 1);
        Maintainance testMaintainance = maintainanceList.get(maintainanceList.size() - 1);
        assertThat(testMaintainance.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testMaintainance.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testMaintainance.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testMaintainance.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createMaintainanceWithExistingId() throws Exception {
        // Create the Maintainance with an existing ID
        maintainance.setId(1L);
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        int databaseSizeBeforeCreate = maintainanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintainanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainanceRepository.findAll().size();
        // set the field null
        maintainance.setLevel(null);

        // Create the Maintainance, which fails.
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        restMaintainanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainanceRepository.findAll().size();
        // set the field null
        maintainance.setDate(null);

        // Create the Maintainance, which fails.
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        restMaintainanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaintainances() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainance.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintainancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(maintainanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintainanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(maintainanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintainancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(maintainanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintainanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(maintainanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMaintainance() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get the maintainance
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL_ID, maintainance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintainance.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getMaintainancesByIdFiltering() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        Long id = maintainance.getId();

        defaultMaintainanceShouldBeFound("id.equals=" + id);
        defaultMaintainanceShouldNotBeFound("id.notEquals=" + id);

        defaultMaintainanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaintainanceShouldNotBeFound("id.greaterThan=" + id);

        defaultMaintainanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaintainanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaintainancesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where level equals to DEFAULT_LEVEL
        defaultMaintainanceShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the maintainanceList where level equals to UPDATED_LEVEL
        defaultMaintainanceShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMaintainancesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultMaintainanceShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the maintainanceList where level equals to UPDATED_LEVEL
        defaultMaintainanceShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMaintainancesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where level is not null
        defaultMaintainanceShouldBeFound("level.specified=true");

        // Get all the maintainanceList where level is null
        defaultMaintainanceShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainancesByLevelContainsSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where level contains DEFAULT_LEVEL
        defaultMaintainanceShouldBeFound("level.contains=" + DEFAULT_LEVEL);

        // Get all the maintainanceList where level contains UPDATED_LEVEL
        defaultMaintainanceShouldNotBeFound("level.contains=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMaintainancesByLevelNotContainsSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where level does not contain DEFAULT_LEVEL
        defaultMaintainanceShouldNotBeFound("level.doesNotContain=" + DEFAULT_LEVEL);

        // Get all the maintainanceList where level does not contain UPDATED_LEVEL
        defaultMaintainanceShouldBeFound("level.doesNotContain=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price equals to DEFAULT_PRICE
        defaultMaintainanceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the maintainanceList where price equals to UPDATED_PRICE
        defaultMaintainanceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMaintainanceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the maintainanceList where price equals to UPDATED_PRICE
        defaultMaintainanceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price is not null
        defaultMaintainanceShouldBeFound("price.specified=true");

        // Get all the maintainanceList where price is null
        defaultMaintainanceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price is greater than or equal to DEFAULT_PRICE
        defaultMaintainanceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintainanceList where price is greater than or equal to UPDATED_PRICE
        defaultMaintainanceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price is less than or equal to DEFAULT_PRICE
        defaultMaintainanceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintainanceList where price is less than or equal to SMALLER_PRICE
        defaultMaintainanceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price is less than DEFAULT_PRICE
        defaultMaintainanceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the maintainanceList where price is less than UPDATED_PRICE
        defaultMaintainanceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where price is greater than DEFAULT_PRICE
        defaultMaintainanceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the maintainanceList where price is greater than SMALLER_PRICE
        defaultMaintainanceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where place equals to DEFAULT_PLACE
        defaultMaintainanceShouldBeFound("place.equals=" + DEFAULT_PLACE);

        // Get all the maintainanceList where place equals to UPDATED_PLACE
        defaultMaintainanceShouldNotBeFound("place.equals=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where place in DEFAULT_PLACE or UPDATED_PLACE
        defaultMaintainanceShouldBeFound("place.in=" + DEFAULT_PLACE + "," + UPDATED_PLACE);

        // Get all the maintainanceList where place equals to UPDATED_PLACE
        defaultMaintainanceShouldNotBeFound("place.in=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where place is not null
        defaultMaintainanceShouldBeFound("place.specified=true");

        // Get all the maintainanceList where place is null
        defaultMaintainanceShouldNotBeFound("place.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainancesByPlaceContainsSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where place contains DEFAULT_PLACE
        defaultMaintainanceShouldBeFound("place.contains=" + DEFAULT_PLACE);

        // Get all the maintainanceList where place contains UPDATED_PLACE
        defaultMaintainanceShouldNotBeFound("place.contains=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where place does not contain DEFAULT_PLACE
        defaultMaintainanceShouldNotBeFound("place.doesNotContain=" + DEFAULT_PLACE);

        // Get all the maintainanceList where place does not contain UPDATED_PLACE
        defaultMaintainanceShouldBeFound("place.doesNotContain=" + UPDATED_PLACE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date equals to DEFAULT_DATE
        defaultMaintainanceShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the maintainanceList where date equals to UPDATED_DATE
        defaultMaintainanceShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMaintainanceShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the maintainanceList where date equals to UPDATED_DATE
        defaultMaintainanceShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date is not null
        defaultMaintainanceShouldBeFound("date.specified=true");

        // Get all the maintainanceList where date is null
        defaultMaintainanceShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date is greater than or equal to DEFAULT_DATE
        defaultMaintainanceShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the maintainanceList where date is greater than or equal to UPDATED_DATE
        defaultMaintainanceShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date is less than or equal to DEFAULT_DATE
        defaultMaintainanceShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the maintainanceList where date is less than or equal to SMALLER_DATE
        defaultMaintainanceShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date is less than DEFAULT_DATE
        defaultMaintainanceShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the maintainanceList where date is less than UPDATED_DATE
        defaultMaintainanceShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        // Get all the maintainanceList where date is greater than DEFAULT_DATE
        defaultMaintainanceShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the maintainanceList where date is greater than SMALLER_DATE
        defaultMaintainanceShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMaintainancesByCarIsEqualToSomething() throws Exception {
        Car car;
        if (TestUtil.findAll(em, Car.class).isEmpty()) {
            maintainanceRepository.saveAndFlush(maintainance);
            car = CarResourceIT.createEntity(em);
        } else {
            car = TestUtil.findAll(em, Car.class).get(0);
        }
        em.persist(car);
        em.flush();
        maintainance.setCar(car);
        maintainanceRepository.saveAndFlush(maintainance);
        Long carId = car.getId();

        // Get all the maintainanceList where car equals to carId
        defaultMaintainanceShouldBeFound("carId.equals=" + carId);

        // Get all the maintainanceList where car equals to (carId + 1)
        defaultMaintainanceShouldNotBeFound("carId.equals=" + (carId + 1));
    }

    @Test
    @Transactional
    void getAllMaintainancesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            maintainanceRepository.saveAndFlush(maintainance);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        maintainance.setUser(user);
        maintainanceRepository.saveAndFlush(maintainance);
        String userId = user.getId();

        // Get all the maintainanceList where user equals to userId
        defaultMaintainanceShouldBeFound("userId.equals=" + userId);

        // Get all the maintainanceList where user equals to "invalid-id"
        defaultMaintainanceShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintainanceShouldBeFound(String filter) throws Exception {
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainance.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintainanceShouldNotBeFound(String filter) throws Exception {
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintainanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaintainance() throws Exception {
        // Get the maintainance
        restMaintainanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintainance() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();

        // Update the maintainance
        Maintainance updatedMaintainance = maintainanceRepository.findById(maintainance.getId()).get();
        // Disconnect from session so that the updates on updatedMaintainance are not directly saved in db
        em.detach(updatedMaintainance);
        updatedMaintainance.level(UPDATED_LEVEL).price(UPDATED_PRICE).place(UPDATED_PLACE).date(UPDATED_DATE);
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(updatedMaintainance);

        restMaintainanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintainanceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
        Maintainance testMaintainance = maintainanceList.get(maintainanceList.size() - 1);
        assertThat(testMaintainance.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMaintainance.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testMaintainance.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testMaintainance.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintainanceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintainanceWithPatch() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();

        // Update the maintainance using partial update
        Maintainance partialUpdatedMaintainance = new Maintainance();
        partialUpdatedMaintainance.setId(maintainance.getId());

        partialUpdatedMaintainance.level(UPDATED_LEVEL).place(UPDATED_PLACE).date(UPDATED_DATE);

        restMaintainanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintainance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintainance))
            )
            .andExpect(status().isOk());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
        Maintainance testMaintainance = maintainanceList.get(maintainanceList.size() - 1);
        assertThat(testMaintainance.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMaintainance.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testMaintainance.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testMaintainance.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMaintainanceWithPatch() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();

        // Update the maintainance using partial update
        Maintainance partialUpdatedMaintainance = new Maintainance();
        partialUpdatedMaintainance.setId(maintainance.getId());

        partialUpdatedMaintainance.level(UPDATED_LEVEL).price(UPDATED_PRICE).place(UPDATED_PLACE).date(UPDATED_DATE);

        restMaintainanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintainance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintainance))
            )
            .andExpect(status().isOk());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
        Maintainance testMaintainance = maintainanceList.get(maintainanceList.size() - 1);
        assertThat(testMaintainance.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMaintainance.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testMaintainance.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testMaintainance.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintainanceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintainance() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceRepository.findAll().size();
        maintainance.setId(count.incrementAndGet());

        // Create the Maintainance
        MaintainanceDTO maintainanceDTO = maintainanceMapper.toDto(maintainance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintainance in the database
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintainance() throws Exception {
        // Initialize the database
        maintainanceRepository.saveAndFlush(maintainance);

        int databaseSizeBeforeDelete = maintainanceRepository.findAll().size();

        // Delete the maintainance
        restMaintainanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintainance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maintainance> maintainanceList = maintainanceRepository.findAll();
        assertThat(maintainanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
