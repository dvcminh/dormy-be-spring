package com.minhvu.review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

  CUSTOMER,
  STAFF,
  MANAGER;
  public static Role lookup(final String id) {
    for (Role enumValue : values()) {
      if (enumValue.name().equalsIgnoreCase(id)) {
        return enumValue;
      }
    }
    throw new RuntimeException(String.format("Invalid value for role type [%s]. " +
            "It should be %s", id, Arrays.asList(Role.values())));
  }
}
