package com.minhvu.friend.model.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

  CUSTOMER(Collections.emptySet()),
  STAFF(Collections.emptySet()),
  MANAGER(Collections.emptySet());

  @Getter
  private final Set<Permission> permissions;
}
