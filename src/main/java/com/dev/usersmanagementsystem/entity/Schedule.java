/*
Author: Ankit Kumar Sharma
 */
package com.dev.usersmanagementsystem.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Schedule {

    private Integer schedule_id;

    private Integer frequency;

    private Long startTimeInMillis;

    private Long endTimeInMillis;

    private List<ExecutionTime> executionTimes;

    private Integer userId;

}
