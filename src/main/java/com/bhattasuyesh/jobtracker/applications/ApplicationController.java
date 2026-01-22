package com.bhattasuyesh.jobtracker.applications;

import com.bhattasuyesh.jobtracker.applications.dto.ApplicationCreateRequest;
import com.bhattasuyesh.jobtracker.applications.dto.ApplicationResponse;
import com.bhattasuyesh.jobtracker.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bhattasuyesh.jobtracker.applications.dto.ApplicationStatusUpdateRequest;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/applications", "/applications/"})
public class ApplicationController {

    private final ApplicationService service;
    private final UserRepository userRepository;
    private final ApplicationStatusHistoryRepository historyRepository;


    public ApplicationController(ApplicationService service, UserRepository userRepository, ApplicationStatusHistoryRepository historyRepository) {
        this.service = service;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    // Helper: convert logged-in email -> userId
    private UUID currentUserId(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in DB"))
                .getId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(@Valid @RequestBody ApplicationCreateRequest req, Authentication auth) {
        UUID userId = currentUserId(auth);
        Application saved = service.create(userId, req);
        return toResponse(saved);
    }

    @GetMapping
    public List<ApplicationResponse> list(Authentication auth) {
        UUID userId = currentUserId(auth);
        return service.list(userId).stream().map(this::toResponse).toList();
    }

    private ApplicationResponse toResponse(Application a) {
        ApplicationResponse r = new ApplicationResponse();
        r.id = a.getId();
        r.company = a.getCompany();
        r.title = a.getTitle();
        r.jobUrl = a.getJobUrl();
        r.location = a.getLocation();
        r.status = a.getStatus();
        r.appliedDate = a.getAppliedDate();
        r.notes = a.getNotes();
        r.createdAt = a.getCreatedAt();
        r.updatedAt = a.getUpdatedAt();
        return r;
    }

    @PatchMapping("/{id}/status")
    public ApplicationResponse updateStatus(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ApplicationStatusUpdateRequest req,
            Authentication auth
    ) {
        UUID userId = currentUserId(auth);
        Application updated = service.updateStatus(userId, id, req);
        return toResponse(updated);
    }

    @GetMapping("/{id}/history")
    public List<ApplicationStatusHistory> history(@PathVariable("id") UUID id, Authentication auth) {
        UUID userId = currentUserId(auth);

        // Proper ownership check
        Application app = service.getOne(userId, id);

        return historyRepository.findByApplicationIdOrderByChangedAtDesc(app.getId());
    }
}
