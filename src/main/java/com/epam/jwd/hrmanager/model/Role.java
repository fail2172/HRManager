package com.epam.jwd.hrmanager.model;

public enum Role {
    ASPIRANT,
    EMPLOYEE,
    ADMINISTRATOR;

    public static Role of(String name){
        for (Role role : values()
             ) {
            if(role.name().equalsIgnoreCase(name)){
                return role;
            }
        }
        return ASPIRANT;
    }
}
