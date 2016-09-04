package net.java.javahub.backend.coffee.boundary;

import net.java.javahub.backend.coffee.entity.Order;
import net.java.javahub.backend.coffee.entity.Type;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;

@Stateless
public class CoffeeShop {

    @PersistenceContext
    EntityManager entityManager;

    public Type createType(final String name) {
        final Type coffeeType = new Type(name);
        return entityManager.merge(coffeeType);
    }

    public List<Type> getTypes() {
        return entityManager.createNamedQuery(Type.QUERY_FIND_ALL, Type.class).getResultList();
    }

    public Type getType(final String id) {
        return entityManager.find(Type.class, id);
    }

    public void deleteType(final String id) {
        final Type type = entityManager.find(Type.class, id);
        if (type == null)
            throw new NoSuchElementException("Could not find coffee type with id " + id);
        // TODO consider constraint type -> order (delete orders then)

        entityManager.remove(type);
        entityManager.flush();
    }

    public List<Order> getOrders() {
        return entityManager.createNamedQuery(Order.QUERY_FIND_ALL, Order.class).getResultList();
    }

    public Order orderCoffee(final String typeId, final int strength) {
        final Type type = entityManager.find(Type.class, typeId);
        if (type == null)
            throw new IllegalArgumentException("Coffee type " + typeId + " not found");

        final Order order = new Order(type, strength);
        return entityManager.merge(order);
    }

    public Order getOrder(final String id) {
        return entityManager.find(Order.class, id);
    }
}
