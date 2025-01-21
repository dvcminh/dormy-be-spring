package com.minhvu.monolithic.entity;


import com.minhvu.monolithic.enums.PostType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private  AppUser user;


    @ManyToMany
    @JoinTable(
            name = "post_tagged_users",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> taggedUSer;


    //change done from here to automatically delete comment and like when post is deleted

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;
    
}


//we use set so in a post user is not tagged more than one times.
//in one post multiple user is tagged
//and one user is tagged in multiple post
