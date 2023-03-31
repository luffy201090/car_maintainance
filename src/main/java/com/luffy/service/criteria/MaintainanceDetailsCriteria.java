package com.luffy.service.criteria;

import com.luffy.domain.enumeration.Action;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.luffy.domain.MaintainanceDetails} entity. This class is used
 * in {@link com.luffy.web.rest.MaintainanceDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maintainance-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintainanceDetailsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Action
     */
    public static class ActionFilter extends Filter<Action> {

        public ActionFilter() {}

        public ActionFilter(ActionFilter filter) {
            super(filter);
        }

        @Override
        public ActionFilter copy() {
            return new ActionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ActionFilter action;

    private BigDecimalFilter price;

    private LongFilter maintainanceId;

    private StringFilter userId;

    private Boolean distinct;

    public MaintainanceDetailsCriteria() {}

    public MaintainanceDetailsCriteria(MaintainanceDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.maintainanceId = other.maintainanceId == null ? null : other.maintainanceId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MaintainanceDetailsCriteria copy() {
        return new MaintainanceDetailsCriteria(this);
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

    public ActionFilter getAction() {
        return action;
    }

    public ActionFilter action() {
        if (action == null) {
            action = new ActionFilter();
        }
        return action;
    }

    public void setAction(ActionFilter action) {
        this.action = action;
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

    public LongFilter getMaintainanceId() {
        return maintainanceId;
    }

    public LongFilter maintainanceId() {
        if (maintainanceId == null) {
            maintainanceId = new LongFilter();
        }
        return maintainanceId;
    }

    public void setMaintainanceId(LongFilter maintainanceId) {
        this.maintainanceId = maintainanceId;
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
        final MaintainanceDetailsCriteria that = (MaintainanceDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(action, that.action) &&
            Objects.equals(price, that.price) &&
            Objects.equals(maintainanceId, that.maintainanceId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, action, price, maintainanceId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintainanceDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (action != null ? "action=" + action + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (maintainanceId != null ? "maintainanceId=" + maintainanceId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
