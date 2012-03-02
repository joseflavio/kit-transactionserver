package com.fap.framework.db;

public final class DbPooledConnection {

    private final String name;

    public DbPooledConnection(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DbPooledConnection [name=" + name + "]";
    }

}// class
