package io.jpass.auth.infrastructure.security;

import io.jpass.auth.infrastructure.entities.PrivilegeEntity;
import io.jpass.auth.infrastructure.entities.RoleEntity;
import io.jpass.auth.infrastructure.entities.UserEntity;
import io.jpass.auth.infrastructure.repositories.UserJpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public DatabaseUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userJpaRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(authorities(user))
                .build();
    }

    private Collection<GrantedAuthority> authorities(UserEntity user) {
        if (user.getRoles() == null) {
            return java.util.List.of();
        }

        return user
                .getRoles()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(this::roleAuthorities)
                .distinct()
                .toList();
    }

    private Stream<GrantedAuthority> roleAuthorities(RoleEntity role) {
        Stream<GrantedAuthority> roleAuthority = Stream.ofNullable(role.getName())
                .map(name -> new SimpleGrantedAuthority("ROLE_" + name));

        Stream<GrantedAuthority> privilegeAuthorities = role.getPrivileges() == null
                ? Stream.empty()
                : role.getPrivileges().stream()
                .filter(Objects::nonNull)
                .map(PrivilegeEntity::getName)
                .filter(Objects::nonNull)
                .map(p -> new SimpleGrantedAuthority(p.getValue()));

        return Stream.concat(roleAuthority, privilegeAuthorities);
    }
}
