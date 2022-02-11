package com.epam.jwd.hrmanager.model;

public class VacancyRequest implements Entity{

    private final Long id;
    private final Vacancy vacancy;
    private final Account account;
    private final VacancyRequestStatus status;

    public VacancyRequest(Long id, Vacancy vacancy, Account account, VacancyRequestStatus status) {
        this.id = id;
        this.vacancy = vacancy;
        this.account = account;
        this.status = status;
    }

    public VacancyRequest(Vacancy vacancy, Account account, VacancyRequestStatus status) {
        this(null, vacancy, account, status);
    }

    public VacancyRequest(Long id, VacancyRequestStatus status) {
        this(id, null, null, status);
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public Account getAccount() {
        return account;
    }

    public VacancyRequestStatus getStatus() {
        return status;
    }

    public VacancyRequest withVacancy(Vacancy vacancy){
        return new VacancyRequest(id, vacancy, account, status);
    }

    public VacancyRequest withAccount(Account account){
        return new VacancyRequest(id, vacancy, account, status);
    }

    public VacancyRequest withStatus(VacancyRequestStatus status){
        return new VacancyRequest(id, vacancy, account, status);
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VacancyRequest that = (VacancyRequest) o;

        if (!id.equals(that.id)) return false;
        if (!vacancy.equals(that.vacancy)) return false;
        if (!account.equals(that.account)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + vacancy.hashCode();
        result = 31 * result + account.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VacancyRequest{" +
                "id=" + id +
                ", vacancy=" + vacancy +
                ", account=" + account +
                ", status=" + status +
                '}';
    }
}
