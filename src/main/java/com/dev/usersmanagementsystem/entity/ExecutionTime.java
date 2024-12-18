/*
Author: Ankit Kumar Sharma
 */
package com.dev.usersmanagementsystem.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ExecutionTime {

    private Integer executionId;

    private Long startTimeInMillis;

    private int scenarioId;

    private int userId;

    private String status;

}
