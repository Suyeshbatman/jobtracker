package com.bhattasuyesh.jobtracker.auth;

import com.bhattasuyesh.jobtracker.auth.dto.RegisterRequest;
import com.bhattasuyesh.jobtracker.users.User;
import com.bhattasuyesh.jobtracker.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.bhattasuyesh.jobtracker.auth.dto.LoginRequest;
import com.bhattasuyesh.jobtracker.auth.dto.AuthResponse;
import com.bhattasuyesh.jobtracker.config.JwtService;
import org.springframework.security.authentication.BadCredentialsException;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegisterRequest request) {

        // 1) Check if email already exists
        if (userRepository.findByEmail(request.email).isPresent()) {
            // For now, simple message. Later we'll return a proper error object.
            throw new IllegalArgumentException("Email already registered");
        }

        // 2) Create new User object
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email);

        // 3) Hash the password before saving
        String hashed = passwordEncoder.encode(request.password);
        user.setPasswordHash(hashed);

        user.setCreatedAt(OffsetDateTime.now());

        // 4) Save to database
        userRepository.save(user);

        // 5) Return a simple success message
        return "User registered";
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(request.password, user.getPasswordHash());
        if (!matches) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.createToken(user.getEmail());
        return new AuthResponse(token);
    }

}
