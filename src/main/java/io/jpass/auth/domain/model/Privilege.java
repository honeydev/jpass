package io.jpass.auth.domain.model;

import io.jpass.auth.infrastructure.entities.PrivilegeEntity;

public record Privilege(
        PrivilegeName name
) {

    public static Privilege fromEntity(PrivilegeEntity privilegeEntity) {
      return new Privilege(privilegeEntity.getName());
    }
}
