package com.epam.jwd.hrmanager.controller;

import com.epam.jwd.hrmanager.controller.impl.PropertyContextImpl;

public interface PropertyContext {

    String get(String name);

    static PropertyContext getInstance() {
        return PropertyContextImpl.getInstance();
    }

}
