package com.dev.usersmanagementsystem.dto;

import com.dev.usersmanagementsystem.entity.Company;
import com.dev.usersmanagementsystem.entity.ExecutionTime;
import com.dev.usersmanagementsystem.entity.Scenario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.dev.usersmanagementsystem.entity.OurUsers;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String email;
    private String password;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;
    private List<ExecutionTime> executionTimeList;
    private Scenario scenario;
    private Company company;
    private String executionId;
    private String status;
}
