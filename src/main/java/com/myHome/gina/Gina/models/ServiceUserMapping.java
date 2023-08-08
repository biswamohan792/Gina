package com.myHome.gina.Gina.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ServiceUserMapping {
    @Id
    private String userId;
    private String serviceId;
}
