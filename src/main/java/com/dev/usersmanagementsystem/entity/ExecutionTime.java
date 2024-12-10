package com.dev.usersmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "executiontime")
@Data
public class ExecutionTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer executionId;

    @Column(name = "start_time_in_millis")
    private Long startTimeInMillis;

    @Column(name = "scenario_id")
    private int scenarioId;

    @Column(name = "user_id")
    private int userId;

    @Column
    private String status;

}
