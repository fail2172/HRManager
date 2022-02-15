package com.epam.jwd.hrmanager.model;

public enum Employment {
    FULL_EMPLOYMENT("full employment"),
    PART_TIME_EMPLOYMENT("part time employment"),
    INTERNSHIP("internship"),
    PROJECT_WORK("project work"),
    VOLUNTEERING("volunteering");

    private final String name;

    Employment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Employment of(String name) {
        for (Employment employment : values()) {
            if (employment.name().equalsIgnoreCase(name)) {
                return employment;
            }
        }
        return FULL_EMPLOYMENT;
    }
}
