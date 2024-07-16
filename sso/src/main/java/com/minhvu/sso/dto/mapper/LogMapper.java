package com.minhvu.sso.dto.mapper;

import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.model.Log;
import com.minhvu.sso.service.UserMapperService;
import com.minhvu.sso.util.CommonUtils;
import org.mapstruct.Mapper;

@Mapper(uses = {CommonUtils.class, UserMapperService.class})
public interface LogMapper {
    LogDto toDto(Log entity);
}
