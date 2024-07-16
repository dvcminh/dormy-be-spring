package com.minhvu.sso.service;

import com.minhvu.sso.dto.model.AppComponentDto;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;

public interface AppComponentService {
    PageData<AppComponentDto> findUserComponents(PageLink pageLink);

    AppComponentDto findByName(String name);
}
