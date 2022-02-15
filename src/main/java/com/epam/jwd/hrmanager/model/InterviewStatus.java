package com.epam.jwd.hrmanager.model;

public enum InterviewStatus {
    PLANNED,
    SUCCESSFULLY,
    UNSUCCESSFULLY;

    public static InterviewStatus of(String name) {
        for (InterviewStatus status : values()
        ) {
            if (status.toString().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return PLANNED;
    }
}
