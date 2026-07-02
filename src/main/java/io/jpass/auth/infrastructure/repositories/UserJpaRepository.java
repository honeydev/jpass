package io.jpass.auth.infrastructure.repositories;

import io.jpass.auth.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
