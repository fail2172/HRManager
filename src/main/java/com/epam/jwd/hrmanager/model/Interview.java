package com.epam.jwd.hrmanager.model;

import java.sql.Date;

public class Interview implements Entity{

    private final Long id;
    private final InterviewStatus status;
    private final Address address;
    private final User user;
    private final Vacancy vacancy;
    private final Date date;

    public Interview(Long id, InterviewStatus status, Address address, User user, Vacancy vacancy, Date date) {
        this.id = id;
        this.status = status;
        this.address = address;
        this.user = user;
        this.vacancy = vacancy;
        this.date = date;
    }

    public Interview(InterviewStatus status, Address address, User user, Vacancy vacancy, Date date) {
        this(null, status, address, user, vacancy, date);
    }

    public Interview(Long id, InterviewStatus status, Date date) {
        this(id, status, null, null, null, date);
    }

    @Override
    public Long getId() {
        return id;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public Address getAddress() {
        return address;
    }

    public User getUser() {
        return user;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public Date getDate() {
        return date;
    }

    public Interview withInterviewStatus(InterviewStatus status) {
        return new Interview(id, status, address, user, vacancy, date);
    }

    public Interview withAddress(Address address) {
        return new Interview(id, status, address, user, vacancy, date);
    }

    public Interview withUser(User user) {
        return new Interview(id, status, address, user, vacancy, date);
    }

    public Interview withVacancy(Vacancy vacancy) {
        return new Interview(id, status, address, user, vacancy, date);
    }

    public Interview withData(Date date) {
        return new Interview(id, status, address, user, vacancy, date);
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", status=" + status +
                ", address=" + address +
                ", user=" + user +
                ", vacancy=" + vacancy +
                ", date=" + date +
                '}';
    }
}
