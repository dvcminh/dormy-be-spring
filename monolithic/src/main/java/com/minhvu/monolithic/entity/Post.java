package com.minhvu.monolithic.entity;


import com.minhvu.monolithic.enums.PostType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "posts")
public class Post extends BaseEntity {

    @Size(max = 200,message = "Caption cannot be more than 200 character")
    private String caption;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @NotNull(message = "Created time is required")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt =LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @NotNull(message = "Reel URL is required")
    @Column(name = "reel_url", nullable = false)
    private String postContent;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private  AppUser user;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tagged_users",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> taggedUSer;
}


//we use set so in a post user is not tagged more than one times.
//in one post multiple user is tagged
//and one user is tagged in multiple post
