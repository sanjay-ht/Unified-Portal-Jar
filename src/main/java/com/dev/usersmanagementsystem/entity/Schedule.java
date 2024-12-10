package com.dev.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    private Integer schedule_id;

    @Column
    private Integer frequency;

    @Column(name = "start_time_in_millis")
    private Long startTimeInMillis;

    @Column(name = "end_time_in_millis")
    private Long endTimeInMillis;

    @OneToMany(mappedBy = "scenarioId")
    private List<ExecutionTime> executionTimes;

    @Column
    private Integer userId;

}
