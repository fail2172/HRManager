package com.epam.jwd.hrmanager.model;

public enum Employment {
    FULL_EMPLOYMENT,
    PART_TIME_EMPLOYMENT,
    INTERNSHIP,
    PROJECT_WORK,
    VOLUNTEERING;

    public static Employment of(String name) {
        for (Employment employment : values()){
            if(employment.name().equals(name)){
                return employment;
            }
        }
        return FULL_EMPLOYMENT;
    }
}
