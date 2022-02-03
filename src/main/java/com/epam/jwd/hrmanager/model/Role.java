package com.epam.jwd.hrmanager.model;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ASPIRANT,
    EMPLOYEE,
    ADMINISTRATOR,
    UNAUTHORIZED;

    private static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

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
}
