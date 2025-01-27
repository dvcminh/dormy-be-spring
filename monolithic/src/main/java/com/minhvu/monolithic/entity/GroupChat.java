package com.minhvu.monolithic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "group_chat")
@Builder
public class GroupChat extends BaseEntity {
    private String name;
    private String image;
    @OneToMany(mappedBy = "groupChat")
    private List<UserGroup> userGroups;
}
