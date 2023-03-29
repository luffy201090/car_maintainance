package com.luffy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.luffy.domain.Car} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String model;

    @NotNull
    private String plate;

    private UserDTO user;

    private BrandDTO brand;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", plate='" + getPlate() + "'" +
            ", user=" + getUser() +
            ", brand=" + getBrand() +
            "}";
    }
}
