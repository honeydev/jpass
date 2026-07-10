package io.jpass.credentials.infrastructure.web;

import io.jpass.auth.infrastructure.repositories.UserJpaRepository;
import io.jpass.credentials.domain.model.CredentialType;
import io.jpass.credentials.usecases.CreateCredentialCommand;
import io.jpass.credentials.usecases.CreateCredentialUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    private final CreateCredentialUseCase createCredentialUseCase;
    private final UserJpaRepository userJpaRepository;

    public CredentialController(
            CreateCredentialUseCase createCredentialUseCase,
            UserJpaRepository userJpaRepository
    ) {
        this.createCredentialUseCase = createCredentialUseCase;
        this.userJpaRepository = userJpaRepository;
    }

    public record CreateCredentialRequest(
            @NotNull CredentialType type,
            String username,
            String password,
            String content
    ) {
    }

    public record CredentialResponse(Long id) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CredentialResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateCredentialRequest request
    ) {
        try {
            Long ownerId = userJpaRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                    .getId();

            Long id = createCredentialUseCase.execute(new CreateCredentialCommand(
                    ownerId,
                    request.type(),
                    request.username(),
                    request.password(),
                    request.content()
            ));

            return new CredentialResponse(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
