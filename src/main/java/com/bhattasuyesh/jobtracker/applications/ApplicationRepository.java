package com.bhattasuyesh.jobtracker.applications;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Application> findByIdAndUserId(UUID id, UUID userId);
}
