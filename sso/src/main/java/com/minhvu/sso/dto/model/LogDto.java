package com.minhvu.sso.dto.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDto {
    private UUID id;
    private UUID entityId;
    private AppUserDto createdBy;
    private JsonNode actionData;
    private ActionStatus actionStatus;
    private ActionType actionType;
    private String actionFailureDetails;
    private Date createdAt;
}
