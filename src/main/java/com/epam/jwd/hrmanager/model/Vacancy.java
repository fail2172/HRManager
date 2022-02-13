package com.epam.jwd.hrmanager.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

public class Vacancy implements Entity{

    private final Long id;
    private final String title;
    private final BigDecimal salary;
    private final Employer employer;
    private final City city;
    private final Employment employment;
    private final Integer experience;
    private final Date date;
    private final String description;

    public Vacancy(Long id, String title, BigDecimal salary, Employer employer, City city,
                   Employment employment, Integer experience, Date date, String description) {
        this.id = id;
        this.title = title;
        this.salary = salary;
        this.employer = employer;
        this.city = city;
        this.employment = employment;
        this.experience = experience;
        this.date = date;
        this.description = description;
    }

    public Vacancy(String title, BigDecimal salary, Employer employer, City city, Employment employment,
                   Integer experience,  Date date, String description) {
        this(null, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy(Long id, String title, BigDecimal salary, Employment employment,
                   Integer experience,  Date date, String description) {
        this(id, title, salary, null, null, employment, experience, date, description);
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

    public Employer getEmployer() {
        return employer;
    }

    public City getCity() {
        return city;
    }

    public Employment getEmployment() {
        return employment;
    }

    public Integer getExperience() {
        return experience;
    }

    public Date getDate() {
        return date;
    }

    public Vacancy withTitle(String title){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withSalary(BigDecimal salary){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withEmployer(Employer employer){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withCity(City city){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withEmployment(Employment employment){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withExperience(Integer experience){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withDate(Date date){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
    }

    public Vacancy withDescription(String description){
        return new Vacancy(id, title, salary, employer, city, employment, experience, date, description);
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
        if (employment != vacancy.employment) return false;
        if (!experience.equals(vacancy.experience)) return false;
        if (!date.equals(vacancy.date)) return false;
        return description.equals(vacancy.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + salary.hashCode();
        result = 31 * result + employer.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + employment.hashCode();
        result = 31 * result + experience.hashCode();
        result = 31 * result + date.hashCode();
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
                ", employment=" + employment +
                ", experience=" + experience +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
