package com.luffy.service.dto;

import com.luffy.domain.enumeration.Action;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.luffy.domain.MaintainanceDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintainanceDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Action action;

    private BigDecimal price;

    private MaintainanceDTO maintainance;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MaintainanceDTO getMaintainance() {
        return maintainance;
    }

    public void setMaintainance(MaintainanceDTO maintainance) {
        this.maintainance = maintainance;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintainanceDetailsDTO)) {
            return false;
        }

        MaintainanceDetailsDTO maintainanceDetailsDTO = (MaintainanceDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintainanceDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintainanceDetailsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", action='" + getAction() + "'" +
            ", price=" + getPrice() +
            ", maintainance=" + getMaintainance() +
            ", user=" + getUser() +
            "}";
    }
}
