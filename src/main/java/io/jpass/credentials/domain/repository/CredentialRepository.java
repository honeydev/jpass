package io.jpass.credentials.domain.repository;

import io.jpass.credentials.domain.model.Credential;

public interface CredentialRepository {
    Credential save(Credential credential);
}
