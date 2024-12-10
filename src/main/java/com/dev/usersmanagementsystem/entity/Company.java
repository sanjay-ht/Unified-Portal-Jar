package com.dev.usersmanagementsystem.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "company")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dbId;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name="company_id")
    private int companyId;

    @Column(name = "db_host")
    private String dbHost;

}
