package io.jpass.auth.domain.model;

import io.jpass.auth.infrastructure.entities.RoleEntity;

import java.util.Collection;

public record Role(
        RoleName name,
        Collection<Privilege> privileges
) {

    public static Role fromEntity(RoleEntity roleEntity) {
        return new Role(
                roleEntity.getName(),
                roleEntity.getPrivileges().stream().map(Privilege::fromEntity).toList()
        );
    }
}
