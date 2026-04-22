package io.jpass.credentials.usecases;

import io.jpass.credentials.domain.model.CredentialType;

public record CreateCredentialCommand(
        String value,
        CredentialType type
) {}
