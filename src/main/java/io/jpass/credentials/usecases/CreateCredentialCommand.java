package io.jpass.credentials.usecases;

import io.jpass.credentials.domain.model.CredentialType;

public record CreateCredentialCommand(
        Long ownerId,
        CredentialType type,
        String username,
        String password,
        String content
) {}
