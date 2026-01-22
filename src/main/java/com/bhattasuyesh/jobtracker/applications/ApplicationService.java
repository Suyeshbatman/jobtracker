package com.bhattasuyesh.jobtracker.applications;

import com.bhattasuyesh.jobtracker.applications.dto.ApplicationCreateRequest;
import org.springframework.stereotype.Service;
import com.bhattasuyesh.jobtracker.applications.dto.ApplicationStatusUpdateRequest;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    private final ApplicationRepository repo;
    private final ApplicationStatusHistoryRepository historyRepo;

    public ApplicationService(ApplicationRepository repo, ApplicationStatusHistoryRepository historyRepo) {
        this.repo = repo;
        this.historyRepo = historyRepo;
    }

    public Application create(UUID userId, ApplicationCreateRequest req) {
        Application a = new Application();
        a.setId(UUID.randomUUID());
        a.setUserId(userId);
        a.setCompany(req.company);
        a.setTitle(req.title);
        a.setJobUrl(req.jobUrl);
        a.setLocation(req.location);
        a.setNotes(req.notes);
        a.setStatus(ApplicationStatus.APPLIED);
        a.setCreatedAt(OffsetDateTime.now());
        a.setUpdatedAt(OffsetDateTime.now());
        return repo.save(a);
    }

    public List<Application> list(UUID userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Application getOne(UUID userId, UUID applicationId) {
        return repo.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    public Application updateStatus(UUID userId, UUID applicationId, ApplicationStatusUpdateRequest req) {
        Application app = repo.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // Ensure user can only update their own application
        if (!app.getUserId().equals(userId)) {
            throw new IllegalStateException("Not allowed");
        }

        ApplicationStatus from = app.getStatus();
        ApplicationStatus to = req.status;

        // Update application
        app.setStatus(to);
        app.setUpdatedAt(OffsetDateTime.now());
        Application saved = repo.save(app);

        // Write history record
        ApplicationStatusHistory h = new ApplicationStatusHistory();
        h.setId(UUID.randomUUID());
        h.setApplicationId(applicationId);
        h.setFromStatus(from);
        h.setToStatus(to);
        h.setChangedAt(OffsetDateTime.now());
        h.setNote(req.note);

        historyRepo.save(h);

        return saved;
    }

}
