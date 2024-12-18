/*
Author: Ankit Kumar Sharma
 */
package com.dev.usersmanagementsystem.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scenario {

    private Integer scenario_id;

    private String code;

    private String description;

    private String jsonFile;

    private String status;

    private int user_id;

    private Schedule schedule;


}
