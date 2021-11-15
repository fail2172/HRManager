package com.epam.jwd.hrmanager.model;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.PrimitiveIterator;

public class Vacancy implements Entity{

    private final Long id;
    private final String title;
    private final BigDecimal salary;
    private final Employer employer;
    private final City city;
    private final String description;

    public Vacancy(Long id, String title, BigDecimal salary, Employer employer, City city, String description) {
        this.id = id;
        this.title = title;
        this.salary = salary;
        this.employer = employer;
        this.city = city;
        this.description = description;
    }

    public Vacancy(String title, BigDecimal salary, Employer employer, City city, String description) {
        this(null, title, salary, employer, city, description);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Optional<String> getDescription() {
        return Optional.of(description);
    }

    public Vacancy withEmployer(Employer employer){
        return new Vacancy(id, title, salary, employer, city, description);
    }

    public Vacancy withCity(City city){
        return new Vacancy(id, title, salary, employer, city, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (!id.equals(vacancy.id)) return false;
        if (!title.equals(vacancy.title)) return false;
        if (!salary.equals(vacancy.salary)) return false;
        if (!employer.equals(vacancy.employer)) return false;
        if (!city.equals(vacancy.city)) return false;
        return description.equals(vacancy.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + salary.hashCode();
        result = 31 * result + employer.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", salary=" + salary +
                ", employer=" + employer +
                ", city=" + city +
                ", description='" + description + '\'' +
                '}';
    }
}
