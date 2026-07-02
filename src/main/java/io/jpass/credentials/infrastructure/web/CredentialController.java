package io.jpass.credentials.infrastructure.web;

import io.jpass.auth.domain.model.RoleName;
import io.jpass.auth.infrastructure.web.AuthController;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credential")
public class CredentialController {

    public record CredentialResponse(Long id) {}

    @PostMapping
    public CredentialResponse create() {

        return new CredentialResponse(1L);
    }
}
