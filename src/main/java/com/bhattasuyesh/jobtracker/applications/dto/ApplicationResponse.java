package com.bhattasuyesh.jobtracker.applications.dto;

import com.bhattasuyesh.jobtracker.applications.ApplicationStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ApplicationResponse {
    public UUID id;
    public String company;
    public String title;
    public String jobUrl;
    public String location;
    public ApplicationStatus status;
    public LocalDate appliedDate;
    public String notes;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
}
