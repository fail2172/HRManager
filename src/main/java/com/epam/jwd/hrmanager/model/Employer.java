package com.epam.jwd.hrmanager.model;

import java.util.Optional;

public class Employer implements Entity {

    private final Long id;
    private final String name;
    private final String description;

    public Employer(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Employer(String name, String description) {
        this(null, name, description);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return Optional.of(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employer employer = (Employer) o;

        if (!id.equals(employer.id)) return false;
        if (!name.equals(employer.name)) return false;
        return description.equals(employer.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
