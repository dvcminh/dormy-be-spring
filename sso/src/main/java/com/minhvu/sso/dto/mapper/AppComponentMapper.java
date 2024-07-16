package com.minhvu.sso.dto.mapper;

import com.minhvu.sso.dto.model.AppComponentDto;
import com.minhvu.sso.model.AppComponent;
import org.mapstruct.Mapper;

@Mapper
public interface AppComponentMapper {

    AppComponent toModel(AppComponentDto componentDto);

    AppComponentDto toDto(AppComponent component);
}
