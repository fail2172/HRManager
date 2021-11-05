package com.epam.jwd.hrmanager.model;

import java.util.Optional;

public class City implements Entity{

    private final Long id;
    private final String name;

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(String name) {
        this(null, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public Optional<Long> getId() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (!id.equals(city.id)) return false;
        return name.equals(city.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
