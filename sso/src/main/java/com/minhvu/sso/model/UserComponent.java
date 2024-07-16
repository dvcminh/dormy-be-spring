package com.minhvu.sso.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_components")
public class UserComponent extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "app_component_id", nullable = false)
    private AppComponent appComponent;

    @ElementCollection(targetClass = String.class)
    private List<String> permissions;

}
