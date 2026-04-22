package io.jpass.credentials.domain.model;

public record Credential(
        String value,
        CredentialType type
) {
}
