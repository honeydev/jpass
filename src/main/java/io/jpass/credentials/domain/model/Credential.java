package io.jpass.credentials.domain.model;

import java.util.Optional;

import static io.jpass.credentials.domain.model.CredentialType.NOTE;
import static io.jpass.credentials.domain.model.CredentialType.PASSWORD;

public record Credential(
        Long id,
        Long ownerId,
        Optional<String> username,
        Optional<String> password,
        Optional<String> content,
        CredentialType type
) {
    public Credential(Long ownerId, Optional<String> username, Optional<String> password) {
        this(null, ownerId, username, password, Optional.empty(), PASSWORD);
    }

    public Credential(Long ownerId, Optional<String> content) {
        this(null, ownerId, Optional.empty(), Optional.empty(), content, NOTE);
    }
}
