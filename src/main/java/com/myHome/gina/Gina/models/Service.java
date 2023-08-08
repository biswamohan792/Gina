package com.myHome.gina.Gina.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Service {
    @Id
    private String serviceId;
    private String name;
    private String email;
    private String password;
    private String details;
    private Timestamp creation;
    private String pic;
}
