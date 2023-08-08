package com.myHome.gina.Gina.models;

import com.myHome.gina.Gina.constants.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    private String userId;
    private String name;
    private String pic;
    private String password;
    private String email;
    private String details;
    private int age;
    private Timestamp creation;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
