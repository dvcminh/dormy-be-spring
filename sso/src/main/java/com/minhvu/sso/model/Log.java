package com.minhvu.sso.model;

import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private UUID createdBy;
    @Column(length = 10485760)
    private String actionData;
    @Enumerated(EnumType.STRING)
    private ActionStatus actionStatus;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    @Column(length = 10485760)
    private String actionFailureDetails;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;
}
