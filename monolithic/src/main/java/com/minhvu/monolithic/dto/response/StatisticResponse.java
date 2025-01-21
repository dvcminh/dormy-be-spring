package com.minhvu.monolithic.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticResponse {
    private int success;
    private int failure;
}
