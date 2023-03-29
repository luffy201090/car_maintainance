package com.luffy.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.luffy.domain.Maintainance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintainanceDTO implements Serializable {

    private Long id;

    @NotNull
    private String level;

    private BigDecimal price;

    private String place;

    @NotNull
    private LocalDate date;

    private CarDTO car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintainanceDTO)) {
            return false;
        }

        MaintainanceDTO maintainanceDTO = (MaintainanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintainanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintainanceDTO{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", price=" + getPrice() +
            ", place='" + getPlace() + "'" +
            ", date='" + getDate() + "'" +
            ", car=" + getCar() +
            "}";
    }
}
