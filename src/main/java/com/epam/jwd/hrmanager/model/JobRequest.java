package com.epam.jwd.hrmanager.model;

public class JobRequest implements Entity{

    private final Long id;
    private final Vacancy vacancy;
    private final Account account;
    private final JobRequestStatus status;

    public JobRequest(Long id, Vacancy vacancy, Account account, JobRequestStatus status) {
        this.id = id;
        this.vacancy = vacancy;
        this.account = account;
        this.status = status;
    }

    public JobRequest(Vacancy vacancy, Account account, JobRequestStatus status) {
        this(null, vacancy, account, status);
    }

    public JobRequest(Long id, JobRequestStatus status) {
        this(id, null, null, status);
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public Account getAccount() {
        return account;
    }

    public JobRequestStatus getStatus() {
        return status;
    }

    public JobRequest withVacancy(Vacancy vacancy){
        return new JobRequest(id, vacancy, account, status);
    }

    public JobRequest withAccount(Account account){
        return new JobRequest(id, vacancy, account, status);
    }

    public JobRequest withStatus(JobRequestStatus status){
        return new JobRequest(id, vacancy, account, status);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobRequest that = (JobRequest) o;

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
