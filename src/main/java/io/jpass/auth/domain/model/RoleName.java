package io.jpass.auth.domain.model;

public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");

    public final String value;

    RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
