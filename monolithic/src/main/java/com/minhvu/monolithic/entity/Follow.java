package com.minhvu.monolithic.entity;


import com.minhvu.monolithic.enums.FollowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "follows")
public class Follow extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "following_id",nullable = false) // The user being followed
    private  AppUser following;

    @ManyToOne
    @JoinColumn(name = "follower_id",nullable = false) // The user initiating the follow request
    private AppUser follower;

    @Enumerated(EnumType.STRING)
    private FollowStatus status;
}

//many follow have one following
//many follow have one follower

//When User A Follows User B:
//id	following_id	follower_id	   status
//1	     B's ID	        A's ID	       ACCEPTED

//following_id: Points to User B, as User B is the one being followed.
//follower_id: Points to User A, as User A is the one doing the following
//in short we can say the user which is followed by other user is added in following table
//and the person who made follow request is added in follower table