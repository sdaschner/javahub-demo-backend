package net.java.javahub.backend.coffee.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "coffee_orders")
@NamedQuery(name = Order.QUERY_FIND_ALL, query = "select o from net.java.javahub.backend.coffee.entity.Order o")
public class Order {

    public static final String QUERY_FIND_ALL = "Order.findAll";

    @Id
    private String id;

    @ManyToOne(optional = false)
    private Type type;

    private int strength;

    // TODO add created date

    public Order() {
    }

    public Order(final Type type, final int strength) {
        this.type = type;
        this.strength = strength;
    }

    @PrePersist
    private void generateId() {
        if (id == null)
            id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(final int strength) {
        this.strength = strength;
    }

}
