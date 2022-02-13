package com.epam.jwd.hrmanager.model;

public enum JobRequestStatus {
    FIELD,
    APPROVED,
    DENIED;

    public static JobRequestStatus of(String name){
        for(JobRequestStatus status : values()){
            if(status.name().equalsIgnoreCase(name)){
                return status;
            }
        }
        return FIELD;
    }
}
