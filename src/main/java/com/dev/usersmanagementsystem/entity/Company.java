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
public class Company {

    private Integer dbId;

    private String dbName;

    private String username;

    private String password;

    private int companyId;

    private String dbHost;

}
