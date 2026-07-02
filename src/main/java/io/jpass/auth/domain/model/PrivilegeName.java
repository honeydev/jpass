package io.jpass.auth.domain.model;

import java.util.Arrays;

public enum PrivilegeName {

    CREATE_CREDENTIAL("CREATE_CREDENTIAL"),
    EDIT_CREDENTIAL("EDIT_CREDENTIAL"),
    DELETE_CREDENTIAL("DELETE_CREDENTIAL");

    private final String value;

    PrivilegeName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public PrivilegeName fromString(String value) {
        return Arrays.stream(values())
                .sequential()
                .filter(v -> v.getValue().equals(value))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Unknown enum %s value: %s ".formatted(this.getClass(), value)
                        )
                );
    }
}
