package cibertec.blog.infrastructure.controllers;

import cibertec.blog.infrastructure.controllers.dto.LoginRequest;
import cibertec.blog.infrastructure.controllers.dto.LoginResponse;
import cibertec.blog.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );

            String token = jwtService.generate(auth);

            
            String role = auth.getAuthorities().stream()
                    .findFirst().map(a -> a.getAuthority())
                    .orElse("ROLE_PERIODISTA");

            return new LoginResponse(token, auth.getName(), role, jwtService.getExpiresInSeconds());

        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos");
        }
    }
}
