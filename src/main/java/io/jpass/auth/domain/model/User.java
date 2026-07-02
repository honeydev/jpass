package io.jpass.auth.domain.model;

import io.jpass.auth.infrastructure.entities.UserEntity;

import java.util.Collection;
import java.util.Date;

public record User(
    Long id,
    String email,
    Date created,
    Date updated,
    Collection<Role> roles
) {

    public boolean hasPrivilege(String privilegeName) {

        return roles.stream().anyMatch(role ->
           role.privileges()
                   .stream()
                   .anyMatch(privilege ->
                       privilege.name().equals(privilegeName)
                   )
        );
    }

    public static User fromEntity(UserEntity user) {
        return new User(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles().stream().map(Role::fromEntity).toList()
        );
    }
}
