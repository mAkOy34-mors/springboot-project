package com.homeservices.app.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String lastName;
    private String address;
}
