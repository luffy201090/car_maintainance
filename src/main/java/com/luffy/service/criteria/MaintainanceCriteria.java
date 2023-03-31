package com.luffy.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.luffy.domain.Maintainance} entity. This class is used
 * in {@link com.luffy.web.rest.MaintainanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintainances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintainanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter level;

    private BigDecimalFilter price;

    private StringFilter place;

    private LocalDateFilter date;

    private LongFilter carId;

    private StringFilter userId;

    private Boolean distinct;

    public MaintainanceCriteria() {}

    public MaintainanceCriteria(MaintainanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.place = other.place == null ? null : other.place.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.carId = other.carId == null ? null : other.carId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MaintainanceCriteria copy() {
        return new MaintainanceCriteria(this);
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

    public StringFilter getLevel() {
        return level;
    }

    public StringFilter level() {
        if (level == null) {
            level = new StringFilter();
        }
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public StringFilter getPlace() {
        return place;
    }

    public StringFilter place() {
        if (place == null) {
            place = new StringFilter();
        }
        return place;
    }

    public void setPlace(StringFilter place) {
        this.place = place;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getCarId() {
        return carId;
    }

    public LongFilter carId() {
        if (carId == null) {
            carId = new LongFilter();
        }
        return carId;
    }

    public void setCarId(LongFilter carId) {
        this.carId = carId;
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
        final MaintainanceCriteria that = (MaintainanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(level, that.level) &&
            Objects.equals(price, that.price) &&
            Objects.equals(place, that.place) &&
            Objects.equals(date, that.date) &&
            Objects.equals(carId, that.carId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, price, place, date, carId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintainanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (place != null ? "place=" + place + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (carId != null ? "carId=" + carId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
