package com.bhattasuyesh.jobtracker.applications.dto;

import jakarta.validation.constraints.NotBlank;

public class ApplicationCreateRequest {

    @NotBlank
    public String company;

    @NotBlank
    public String title;

    public String jobUrl;
    public String location;
    public String notes;
}
