package com.epam.jwd.hrmanager.model;

public class User implements Entity {

    private final Long id;
    private final String firstName;
    private final String secondName;

    public User(Long id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public User(String firstName, String secondName) {
        this(null, firstName, secondName);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public User withRole(Role role) {
        return new User(id, firstName, secondName);
    }

    public User withFirstName(String firstName) {
        return new User(id, firstName, secondName);
    }

    public User withSecondName(String secondName) {
        return new User(id, firstName, secondName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!firstName.equals(user.firstName)) return false;
        return secondName.equals(user.secondName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
