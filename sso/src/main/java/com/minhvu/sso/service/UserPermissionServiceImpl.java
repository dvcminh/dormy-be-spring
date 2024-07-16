package com.minhvu.sso.service;

import com.minhvu.sso.exception.BadRequestException;
import com.minhvu.sso.model.AppComponent;
import com.minhvu.sso.model.AppUser;
import com.minhvu.sso.model.UserComponent;
import com.minhvu.sso.repository.AppComponentRepository;
import com.minhvu.sso.repository.AppUserRepository;
import com.minhvu.sso.repository.UserComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final String USER_NOT_FOUND = "User with id [%s] is not found";

    private final AppComponentRepository appComponentRepository;

    private final UserComponentRepository userComponentRepository;
    private final AppUserRepository userRepository;


    public boolean validateUserPermission(UUID userId, String url, String method) {
        AppComponent component = appComponentRepository.findByUrlBase(url);
        UserComponent userComponent = userComponentRepository
                .findByAppComponentIdAndUserId(component.getId(), userId);
        if (userComponent != null) {
            List<String> permissionsList = userComponent.getPermissions();
            List<String> permissions = convertMethodToPermission(method);
            List<String> checklist = new ArrayList<>();
            assert permissions != null;
            permissions.forEach(permission -> {
                if (permissionsList.contains(permission)) checklist.add(permission);
            });
            return !checklist.isEmpty();
        }
        return false;
    }

    private boolean checkExpiredIn(LocalDateTime expired) {
        if (expired.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    private List<String> convertMethodToPermission(String method) {
        switch (method) {
            case "GET":
                return List.of("READ");
            case "POST":
                return List.of("ADD", "EDIT");
            case "PUT":
                return List.of("EDIT");
            default:
                return null;
        }
    }

}
