package com.luffy.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.luffy.domain.Car} entity. This class is used
 * in {@link com.luffy.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter model;

    private StringFilter plate;

    private StringFilter userId;

    private LongFilter brandId;

    private Boolean distinct;

    public CarCriteria() {}

    public CarCriteria(CarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.plate = other.plate == null ? null : other.plate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.brandId = other.brandId == null ? null : other.brandId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getPlate() {
        return plate;
    }

    public StringFilter plate() {
        if (plate == null) {
            plate = new StringFilter();
        }
        return plate;
    }

    public void setPlate(StringFilter plate) {
        this.plate = plate;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public LongFilter getBrandId() {
        return brandId;
    }

    public LongFilter brandId() {
        if (brandId == null) {
            brandId = new LongFilter();
        }
        return brandId;
    }

    public void setBrandId(LongFilter brandId) {
        this.brandId = brandId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CarCriteria that = (CarCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(model, that.model) &&
            Objects.equals(plate, that.plate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, model, plate, userId, brandId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (plate != null ? "plate=" + plate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (brandId != null ? "brandId=" + brandId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
