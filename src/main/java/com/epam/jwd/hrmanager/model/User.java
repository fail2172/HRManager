package com.epam.jwd.hrmanager.model;

import java.util.Objects;
import java.util.Optional;

public class User implements Entity{

    private final Long id;
    private final String login;
    private final String email;
    private final Name name;
    private final int age;

    public User(Long id, String login, String email, Name name, int age) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public User(String login, String email, Name name, int age) {
        this(null, login, email, name, age);
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public Name getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!login.equals(user.login)) return false;
        if (!email.equals(user.email)) return false;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.requireNonNull(id).hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", name=" + name +
                ", age=" + age +
                '}';
    }
}
