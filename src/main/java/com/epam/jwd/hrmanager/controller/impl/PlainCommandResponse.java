package com.epam.jwd.hrmanager.controller.impl;

import com.epam.jwd.hrmanager.controller.CommandResponse;

public class PlainCommandResponse implements CommandResponse {

    private final boolean redirect;
    private final String path;

    public PlainCommandResponse(String path) {
        this(false, path);
    }

    public PlainCommandResponse(boolean redirect, String path) {
        this.redirect = redirect;
        this.path = path;
    }

    @Override
    public boolean isRedirect() {
        return redirect;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlainCommandResponse that = (PlainCommandResponse) o;

        if (redirect != that.redirect) return false;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        int result = (redirect ? 1 : 0);
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlainCommandResponse{" +
                "redirect=" + redirect +
                ", path='" + path + '\'' +
                '}';
    }
}
