package com.luffy.web.rest;

import static com.luffy.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luffy.IntegrationTest;
import com.luffy.domain.Maintainance;
import com.luffy.domain.MaintainanceDetails;
import com.luffy.domain.User;
import com.luffy.domain.enumeration.Action;
import com.luffy.repository.MaintainanceDetailsRepository;
import com.luffy.service.MaintainanceDetailsService;
import com.luffy.service.criteria.MaintainanceDetailsCriteria;
import com.luffy.service.dto.MaintainanceDetailsDTO;
import com.luffy.service.mapper.MaintainanceDetailsMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MaintainanceDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MaintainanceDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Action DEFAULT_ACTION = Action.I;
    private static final Action UPDATED_ACTION = Action.R;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/maintainance-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaintainanceDetailsRepository maintainanceDetailsRepository;

    @Mock
    private MaintainanceDetailsRepository maintainanceDetailsRepositoryMock;

    @Autowired
    private MaintainanceDetailsMapper maintainanceDetailsMapper;

    @Mock
    private MaintainanceDetailsService maintainanceDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintainanceDetailsMockMvc;

    private MaintainanceDetails maintainanceDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintainanceDetails createEntity(EntityManager em) {
        MaintainanceDetails maintainanceDetails = new MaintainanceDetails().name(DEFAULT_NAME).action(DEFAULT_ACTION).price(DEFAULT_PRICE);
        // Add required entity
        Maintainance maintainance;
        if (TestUtil.findAll(em, Maintainance.class).isEmpty()) {
            maintainance = MaintainanceResourceIT.createEntity(em);
            em.persist(maintainance);
            em.flush();
        } else {
            maintainance = TestUtil.findAll(em, Maintainance.class).get(0);
        }
        maintainanceDetails.setMaintainance(maintainance);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        maintainanceDetails.setUser(user);
        return maintainanceDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintainanceDetails createUpdatedEntity(EntityManager em) {
        MaintainanceDetails maintainanceDetails = new MaintainanceDetails().name(UPDATED_NAME).action(UPDATED_ACTION).price(UPDATED_PRICE);
        // Add required entity
        Maintainance maintainance;
        if (TestUtil.findAll(em, Maintainance.class).isEmpty()) {
            maintainance = MaintainanceResourceIT.createUpdatedEntity(em);
            em.persist(maintainance);
            em.flush();
        } else {
            maintainance = TestUtil.findAll(em, Maintainance.class).get(0);
        }
        maintainanceDetails.setMaintainance(maintainance);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        maintainanceDetails.setUser(user);
        return maintainanceDetails;
    }

    @BeforeEach
    public void initTest() {
        maintainanceDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createMaintainanceDetails() throws Exception {
        int databaseSizeBeforeCreate = maintainanceDetailsRepository.findAll().size();
        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);
        restMaintainanceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        MaintainanceDetails testMaintainanceDetails = maintainanceDetailsList.get(maintainanceDetailsList.size() - 1);
        assertThat(testMaintainanceDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaintainanceDetails.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testMaintainanceDetails.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createMaintainanceDetailsWithExistingId() throws Exception {
        // Create the MaintainanceDetails with an existing ID
        maintainanceDetails.setId(1L);
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        int databaseSizeBeforeCreate = maintainanceDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintainanceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainanceDetailsRepository.findAll().size();
        // set the field null
        maintainanceDetails.setName(null);

        // Create the MaintainanceDetails, which fails.
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        restMaintainanceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintainanceDetailsRepository.findAll().size();
        // set the field null
        maintainanceDetails.setAction(null);

        // Create the MaintainanceDetails, which fails.
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        restMaintainanceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetails() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainanceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintainanceDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(maintainanceDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintainanceDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(maintainanceDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMaintainanceDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(maintainanceDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaintainanceDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(maintainanceDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMaintainanceDetails() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get the maintainanceDetails
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, maintainanceDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintainanceDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getMaintainanceDetailsByIdFiltering() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        Long id = maintainanceDetails.getId();

        defaultMaintainanceDetailsShouldBeFound("id.equals=" + id);
        defaultMaintainanceDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultMaintainanceDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaintainanceDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultMaintainanceDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaintainanceDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where name equals to DEFAULT_NAME
        defaultMaintainanceDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the maintainanceDetailsList where name equals to UPDATED_NAME
        defaultMaintainanceDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMaintainanceDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the maintainanceDetailsList where name equals to UPDATED_NAME
        defaultMaintainanceDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where name is not null
        defaultMaintainanceDetailsShouldBeFound("name.specified=true");

        // Get all the maintainanceDetailsList where name is null
        defaultMaintainanceDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByNameContainsSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where name contains DEFAULT_NAME
        defaultMaintainanceDetailsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the maintainanceDetailsList where name contains UPDATED_NAME
        defaultMaintainanceDetailsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where name does not contain DEFAULT_NAME
        defaultMaintainanceDetailsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the maintainanceDetailsList where name does not contain UPDATED_NAME
        defaultMaintainanceDetailsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where action equals to DEFAULT_ACTION
        defaultMaintainanceDetailsShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the maintainanceDetailsList where action equals to UPDATED_ACTION
        defaultMaintainanceDetailsShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultMaintainanceDetailsShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the maintainanceDetailsList where action equals to UPDATED_ACTION
        defaultMaintainanceDetailsShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where action is not null
        defaultMaintainanceDetailsShouldBeFound("action.specified=true");

        // Get all the maintainanceDetailsList where action is null
        defaultMaintainanceDetailsShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price equals to DEFAULT_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the maintainanceDetailsList where price equals to UPDATED_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the maintainanceDetailsList where price equals to UPDATED_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price is not null
        defaultMaintainanceDetailsShouldBeFound("price.specified=true");

        // Get all the maintainanceDetailsList where price is null
        defaultMaintainanceDetailsShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price is greater than or equal to DEFAULT_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintainanceDetailsList where price is greater than or equal to UPDATED_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price is less than or equal to DEFAULT_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintainanceDetailsList where price is less than or equal to SMALLER_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price is less than DEFAULT_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the maintainanceDetailsList where price is less than UPDATED_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        // Get all the maintainanceDetailsList where price is greater than DEFAULT_PRICE
        defaultMaintainanceDetailsShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the maintainanceDetailsList where price is greater than SMALLER_PRICE
        defaultMaintainanceDetailsShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByMaintainanceIsEqualToSomething() throws Exception {
        Maintainance maintainance;
        if (TestUtil.findAll(em, Maintainance.class).isEmpty()) {
            maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);
            maintainance = MaintainanceResourceIT.createEntity(em);
        } else {
            maintainance = TestUtil.findAll(em, Maintainance.class).get(0);
        }
        em.persist(maintainance);
        em.flush();
        maintainanceDetails.setMaintainance(maintainance);
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);
        Long maintainanceId = maintainance.getId();

        // Get all the maintainanceDetailsList where maintainance equals to maintainanceId
        defaultMaintainanceDetailsShouldBeFound("maintainanceId.equals=" + maintainanceId);

        // Get all the maintainanceDetailsList where maintainance equals to (maintainanceId + 1)
        defaultMaintainanceDetailsShouldNotBeFound("maintainanceId.equals=" + (maintainanceId + 1));
    }

    @Test
    @Transactional
    void getAllMaintainanceDetailsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        maintainanceDetails.setUser(user);
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);
        String userId = user.getId();

        // Get all the maintainanceDetailsList where user equals to userId
        defaultMaintainanceDetailsShouldBeFound("userId.equals=" + userId);

        // Get all the maintainanceDetailsList where user equals to "invalid-id"
        defaultMaintainanceDetailsShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintainanceDetailsShouldBeFound(String filter) throws Exception {
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintainanceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintainanceDetailsShouldNotBeFound(String filter) throws Exception {
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintainanceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaintainanceDetails() throws Exception {
        // Get the maintainanceDetails
        restMaintainanceDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintainanceDetails() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();

        // Update the maintainanceDetails
        MaintainanceDetails updatedMaintainanceDetails = maintainanceDetailsRepository.findById(maintainanceDetails.getId()).get();
        // Disconnect from session so that the updates on updatedMaintainanceDetails are not directly saved in db
        em.detach(updatedMaintainanceDetails);
        updatedMaintainanceDetails.name(UPDATED_NAME).action(UPDATED_ACTION).price(UPDATED_PRICE);
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(updatedMaintainanceDetails);

        restMaintainanceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintainanceDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
        MaintainanceDetails testMaintainanceDetails = maintainanceDetailsList.get(maintainanceDetailsList.size() - 1);
        assertThat(testMaintainanceDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaintainanceDetails.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testMaintainanceDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintainanceDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintainanceDetailsWithPatch() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();

        // Update the maintainanceDetails using partial update
        MaintainanceDetails partialUpdatedMaintainanceDetails = new MaintainanceDetails();
        partialUpdatedMaintainanceDetails.setId(maintainanceDetails.getId());

        partialUpdatedMaintainanceDetails.name(UPDATED_NAME).action(UPDATED_ACTION);

        restMaintainanceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintainanceDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintainanceDetails))
            )
            .andExpect(status().isOk());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
        MaintainanceDetails testMaintainanceDetails = maintainanceDetailsList.get(maintainanceDetailsList.size() - 1);
        assertThat(testMaintainanceDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaintainanceDetails.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testMaintainanceDetails.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateMaintainanceDetailsWithPatch() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();

        // Update the maintainanceDetails using partial update
        MaintainanceDetails partialUpdatedMaintainanceDetails = new MaintainanceDetails();
        partialUpdatedMaintainanceDetails.setId(maintainanceDetails.getId());

        partialUpdatedMaintainanceDetails.name(UPDATED_NAME).action(UPDATED_ACTION).price(UPDATED_PRICE);

        restMaintainanceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintainanceDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintainanceDetails))
            )
            .andExpect(status().isOk());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
        MaintainanceDetails testMaintainanceDetails = maintainanceDetailsList.get(maintainanceDetailsList.size() - 1);
        assertThat(testMaintainanceDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaintainanceDetails.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testMaintainanceDetails.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintainanceDetailsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintainanceDetails() throws Exception {
        int databaseSizeBeforeUpdate = maintainanceDetailsRepository.findAll().size();
        maintainanceDetails.setId(count.incrementAndGet());

        // Create the MaintainanceDetails
        MaintainanceDetailsDTO maintainanceDetailsDTO = maintainanceDetailsMapper.toDto(maintainanceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintainanceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintainanceDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintainanceDetails in the database
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintainanceDetails() throws Exception {
        // Initialize the database
        maintainanceDetailsRepository.saveAndFlush(maintainanceDetails);

        int databaseSizeBeforeDelete = maintainanceDetailsRepository.findAll().size();

        // Delete the maintainanceDetails
        restMaintainanceDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintainanceDetails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaintainanceDetails> maintainanceDetailsList = maintainanceDetailsRepository.findAll();
        assertThat(maintainanceDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
