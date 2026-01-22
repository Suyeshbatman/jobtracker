package com.bhattasuyesh.jobtracker.applications.dto;

import com.bhattasuyesh.jobtracker.applications.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class ApplicationStatusUpdateRequest {

    @NotNull
    public ApplicationStatus status;

    public String note;
}

