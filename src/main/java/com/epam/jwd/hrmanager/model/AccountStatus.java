package com.epam.jwd.hrmanager.model;

public enum AccountStatus {
    UNBANNED,
    BANNED;

    public static AccountStatus of(String name){
        if(BANNED.name().equalsIgnoreCase(name)){
            return BANNED;
        } else {
            return UNBANNED;
        }
    }
}
