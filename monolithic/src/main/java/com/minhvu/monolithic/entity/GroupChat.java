package com.minhvu.monolithic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "group_chat")
@Builder
public class GroupChat extends BaseEntity {
    private String name;
    private String image;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false) // This ensures every group has a host
    private AppUser host;
}
