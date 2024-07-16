package com.minhvu.sso.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_component")
public class AppComponent extends BaseEntity {
    private String name;
    private String description;
    private String urlBase;
}
