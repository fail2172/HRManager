package com.epam.jwd.hrmanager.model;

public class Account implements Entity {

    private final Long id;
    private final String login;
    private final String email;
    private final String password;
    private final User user;
    private final Role role;

    public Account(Long id, String login, String email, String password, User user, Role role) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.user = user;
        this.role = role;
    }

    public Account(String login, String email, String password, User user, Role role) {
        this(null, login, email, password, user, role);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

    public Account withLogin(String login) {
        return new Account(id, login, email, password, user, role);
    }

    public Account withEmail(String email) {
        return new Account(id, login, email, password, user, role);
    }

    public Account withPassword(String password) {
        return new Account(id, login, email, password, user, role);
    }

    public Account withUser(User user) {
        return new Account(id, login, email, password, user, role);
    }

    public Account withRole(Role role) {
        return new Account(id, login, email, password, user, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;
        if (!login.equals(account.login)) return false;
        if (!email.equals(account.email)) return false;
        if (!password.equals(account.password)) return false;
        if (!user.equals(account.user)) return false;
        return role == account.role;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user=" + user +
                ", role=" + role +
                '}';
    }
}
