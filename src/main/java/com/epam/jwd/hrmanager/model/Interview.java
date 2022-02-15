package com.epam.jwd.hrmanager.model;

import java.sql.Date;
import java.sql.Time;

public class Interview implements Entity {

    private final Long id;
    private final InterviewStatus status;
    private final Address address;
    private final User user;
    private final Vacancy vacancy;
    private final Date date;
    private final Time time;

    public Interview(Long id, InterviewStatus status, Address address, User user, Vacancy vacancy, Date date, Time time) {
        this.id = id;
        this.status = status;
        this.address = address;
        this.user = user;
        this.vacancy = vacancy;
        this.date = date;
        this.time = time;
    }

    public Interview(InterviewStatus status, Address address, User user, Vacancy vacancy, Date date, Time time) {
        this(null, status, address, user, vacancy, date, time);
    }

    public Interview(Long id, InterviewStatus status, Date date, Time time) {
        this(id, status, null, null, null, date, time);
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

    public Time getTime() {
        return time;
    }

    public Interview withInterviewStatus(InterviewStatus status) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    public Interview withAddress(Address address) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    public Interview withUser(User user) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    public Interview withVacancy(Vacancy vacancy) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    public Interview withData(Date date) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    public Interview withTime(Time time) {
        return new Interview(id, status, address, user, vacancy, date, time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interview interview = (Interview) o;

        if (!id.equals(interview.id)) return false;
        if (status != interview.status) return false;
        if (!address.equals(interview.address)) return false;
        if (!user.equals(interview.user)) return false;
        if (!vacancy.equals(interview.vacancy)) return false;
        if (!date.equals(interview.date)) return false;
        return time.equals(interview.time);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + vacancy.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        return result;
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
                ", time=" + time +
                '}';
    }
}
