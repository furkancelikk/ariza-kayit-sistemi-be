package com.furkancelik.arizakayitsistemi.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    PERSONNEL("PERSONNEL"),
    USER("USER");

    public final String label;

    private UserRole(String label) {
        this.label = label;
    }
}
