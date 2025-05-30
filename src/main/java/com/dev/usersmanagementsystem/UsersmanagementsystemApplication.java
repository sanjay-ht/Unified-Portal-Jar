/*
Author: Ankit Kumar Sharma
 */
package com.dev.usersmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class UsersmanagementsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersmanagementsystemApplication.class, args);
    }

}
