package io.jpass.auth.infrastructure.repositories;

import io.jpass.auth.domain.model.RoleName;
import io.jpass.auth.infrastructure.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleName name);
}
