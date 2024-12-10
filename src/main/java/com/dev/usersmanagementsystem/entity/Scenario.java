package com.dev.usersmanagementsystem.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="Scenario")
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scenario_id;

    @Column
    private String code;

    @Column
    private String description;

    @Column(columnDefinition = "json")
    private String jsonFile;

    @Column
    private String status;

    @Column
    private int user_id;

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


}
