package com.luffy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.luffy.domain.enumeration.Action;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MaintainanceDetails.
 */
@Entity
@Table(name = "maintainance_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintainanceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private Action action;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Maintainance maintainance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaintainanceDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MaintainanceDetails name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return this.action;
    }

    public MaintainanceDetails action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MaintainanceDetails price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Maintainance getMaintainance() {
        return this.maintainance;
    }

    public void setMaintainance(Maintainance maintainance) {
        this.maintainance = maintainance;
    }

    public MaintainanceDetails maintainance(Maintainance maintainance) {
        this.setMaintainance(maintainance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintainanceDetails)) {
            return false;
        }
        return id != null && id.equals(((MaintainanceDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintainanceDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", action='" + getAction() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
