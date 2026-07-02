package io.jpass.auth.infrastructure.entities;

import io.jpass.auth.domain.model.PrivilegeName;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeName name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleEntity> roles;

    public PrivilegeName getName() {
        return name;
    }
}
