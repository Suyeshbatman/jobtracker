package com.bhattasuyesh.jobtracker.common;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/me")
    public String me(Authentication auth) {
        // auth.getName() will be the email we set in JwtAuthFilter
        return "You are: " + auth.getName();
    }
}
