package com.epam.jwd.hrmanager.model;

import java.util.Optional;

public class Street implements Entity {

    private final Long id;
    private final String name;

    public Street(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Street(String name) {
        this(null, name);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Street street = (Street) o;

        if (!id.equals(street.id)) return false;
        return name.equals(street.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Street{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
