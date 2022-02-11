package com.epam.jwd.hrmanager.model;

public enum VacancyRequestStatus {
    FIELD,
    APPROVED,
    DENIED;

    public static VacancyRequestStatus of(String name){
        for(VacancyRequestStatus status : values()){
            if(status.name().equalsIgnoreCase(name)){
                return status;
            }
        }
        return FIELD;
    }
}
