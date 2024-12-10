package com.dev.usersmanagementsystem.service;

import com.dev.usersmanagementsystem.App;
import com.dev.usersmanagementsystem.dto.ReqRes;
import com.dev.usersmanagementsystem.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class UsersManagementService {
    private final String baseUrl = "http://13.126.48.191:8080";

    public List<ExecutionTime> getExecutionTimeList(Long utcMilliseconds) {
        String apiUrl = baseUrl + "/api/get-execution-by-time?utcMilliseconds=" + utcMilliseconds;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        List<ExecutionTime> executionTimes = new ArrayList<>();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            executionTimes = objectMapper.readValue(
                    response.body(),
                    new TypeReference<ReqRes>() {
                    }
            ).getExecutionTimeList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return executionTimes;

    }

    public Scenario getScenarioByUserIdAndScenarioId(int scenarioId, int userId) {
        String apiUrl = baseUrl + "/api/get-scenario-by-userId?userId=" + userId + "&" + "scenarioId=" + scenarioId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        Scenario scenarioOptional = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            scenarioOptional = objectMapper.readValue(
                    response.body(),
                    new TypeReference<ReqRes>() {
                    }
            ).getScenario();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return scenarioOptional;

    }

    public Company getCompanyByUserId(int userId) {
        String apiUrl = baseUrl + "/api/get-company-by-userId?userId=" + userId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        Company company = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            company = objectMapper.readValue(
                    response.body(),
                    new TypeReference<ReqRes>() {
                    }
            ).getCompany();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return company;

    }

    public void setExecutionStatus(int executionId, String status, int userId) {
        String apiUrl = baseUrl + "/api/mark-execution-status?executionId=" + executionId + "&status=" + status + "&userId=" + userId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.print(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Scheduled(fixedRate = 60000)
    public void scheduleExecution() {
        Long currentTimeInMillis = Instant.now().toEpochMilli() + 19800000;
        Instant instant = Instant.ofEpochMilli(currentTimeInMillis);

        ZonedDateTime utcDateTime = instant.atZone(ZoneId.of("UTC"));
        utcDateTime = utcDateTime.withSecond(0).withNano(0);

        long utcMilliseconds = utcDateTime.toInstant().toEpochMilli();

        System.out.println("Hello===" + utcMilliseconds);

        List<ExecutionTime> executionTimes = getExecutionTimeList(utcMilliseconds) == null ? new ArrayList<>() : getExecutionTimeList(utcMilliseconds);
        //Need to hardcode the company if we want
        System.out.println("Execution time" + executionTimes);
        for (ExecutionTime executionTime : executionTimes) {
            Scenario scenario = getScenarioByUserIdAndScenarioId(executionTime.getScenarioId(), executionTime.getUserId());
            System.out.println("New scenario" + scenario);
            if (scenario != null && executionTime.getStatus().equals("Active")) {
                String jsonContent = scenario.getJsonFile();
                Company company = getCompanyByUserId(executionTime.getUserId());
                String password = "";
                String username = "";
                String dbhost = "";
                String dbName = "";
                if (company != null) {
                    password = company.getPassword();
                    username = company.getUsername();
                    dbhost = company.getDbHost();
                    dbName = company.getDbName();
                }

                App obj = new App();
                try {
                    setExecutionStatus(executionTime.getExecutionId(), executionTime.getStatus(), executionTime.getUserId());
                    obj.setup(password, username, dbhost, dbName);
                    obj.runCode(jsonContent, executionTime.getUserId());

                } catch (Exception e) {
                    System.out.println("Exception occurred while executing json" + e);
                }
                System.out.println("Time match" + scenario.getJsonFile());
            }

        }
    }
}
