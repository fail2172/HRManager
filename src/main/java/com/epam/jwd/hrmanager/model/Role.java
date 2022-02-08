package com.epam.jwd.hrmanager.model;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMINISTRATOR(2),
    EMPLOYEE(1),
    ASPIRANT(0),
    UNAUTHORIZED(0);

    private final int securityLevel;

    private static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    Role(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public static List<Role> valuesAsList(){
        return ALL_AVAILABLE_ROLES;
    }

    public static Role of(String name){
        for (Role role : values()
             ) {
            if(role.name().equalsIgnoreCase(name)){
                return role;
            }
        }
        return ASPIRANT;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }
}
