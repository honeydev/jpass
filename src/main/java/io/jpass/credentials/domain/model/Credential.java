package io.jpass.credentials.domain.model;

import java.util.Optional;

import static io.jpass.credentials.domain.model.CredentialType.NOTE;
import static io.jpass.credentials.domain.model.CredentialType.PASSWORD;

public record Credential(
        Optional<String> username,
        Optional<String> password,
        Optional<String> content,
        CredentialType type
) {
    public Credential(Optional<String> username, Optional<String> password) {
        this(username, password, Optional.empty(), PASSWORD);
    }

    public Credential(Optional<String> content) {
        this(Optional.empty(), Optional.empty(), content, NOTE);
    }
}
