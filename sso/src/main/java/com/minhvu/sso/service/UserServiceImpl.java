package com.minhvu.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.sso.dto.mapper.AppComponentMapper;
import com.minhvu.sso.dto.mapper.AppUserMapper;
import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.dto.request.SignUpRequest;
import com.minhvu.sso.dto.request.UserActivateRequest;
import com.minhvu.sso.dto.response.UserProfileResponse;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;
import com.minhvu.sso.exception.BadRequestException;
import com.minhvu.sso.exception.NotFoundException;
import com.minhvu.sso.kafka.UserProducer;
import com.minhvu.sso.model.*;
import com.minhvu.sso.model.enums.*;
import com.minhvu.sso.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;

    private final AppComponentRepository componentRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    private final UserCredentialsService userCredentialsService;



    private final AppUserMapper userMapper;


    private final AppComponentMapper componentMapper;

    private final UserProducer userProducer;



    @Autowired
    LogService logService;
    @Override
    public UserProfileResponse getUserProfile(UUID id) {
        AppUser user = checkUserId(id);
        UserProfileResponse userProfile = userMapper.toUserProfile(user);
        userProfile.setEnabled(userCredentialsService.isEnabled(id));
        return userProfile;
    }
    @Override
    public AppUserDto save(AppUserDto userDto, AppUserDto currentUser) {

        ActionType actionType = userDto.getId() == null ? ActionType.CREATED : ActionType.UPDATED;

        AppUser user = new AppUser();

        if (actionType.equals(ActionType.CREATED)) {
            checkIfEmailExist(userDto.getEmail(), currentUser, userDto);
        } else {
            user = userMapper.toModel(userDto);
        }

        BeanUtils.copyProperties(userDto, user,
                "createdAt", "createdBy"
        );

        if (currentUser != null) {
            if (actionType.equals(ActionType.CREATED)) {
                user.setCreatedBy(currentUser.getId());
            }
        }

        user.setAuthority(AuthorityType.lookup(userDto.getAuthority()));
        user.setRole(RoleType.lookup(userDto.getRole()));

        if (currentUser != null)
            user.setUpdatedBy(currentUser.getId());

        AppUser savedUser = userRepository.saveAndFlush(user);

        if (savedUser.getAuthority().equals(AuthorityType.CUSTOMER_USER)) {
            savedUser = userRepository.saveAndFlush(savedUser);
        }


        if (actionType.equals(ActionType.CREATED)) {
            userCredentialsService.setPassword(savedUser.getId());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityId(savedUser.getId())
                .actionStatus(ActionStatus.SUCCESS)
                .actionType(actionType)
                .actionData(objectMapper.valueToTree(userDto)).build(), currentUser);

        AppUserDto savedUserDto = userMapper.toDto(savedUser);
        userProducer.sendMessage(savedUserDto);
        return savedUserDto;
    }

    @Override
    public AppUserDto signUp(SignUpRequest signUpRequest) {
        checkIfEmailExist(signUpRequest.getEmail(), null, signUpRequest);

        AppUser user = new AppUser();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setAuthority(AuthorityType.CUSTOMER_USER);
        user.setRole(RoleType.CUSTOMER);

        AppUser savedUser = userRepository.saveAndFlush(user);
        AppUserDto savedUserDto = userMapper.toDto(savedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityId(savedUserDto.getId())
                .actionStatus(ActionStatus.SUCCESS)
                .actionType(ActionType.CREATED)
                .actionData(objectMapper.valueToTree(signUpRequest))
                .build(), savedUserDto);


        userProducer.sendMessage(savedUserDto);
        return savedUserDto;
    }

    @Override
    public PageData<?> findUsers(
            PageLink pageLink, RoleType role,
            UUID contactId, AppUserDto currentUser,
            Long createdAtStartTs, Long createdAtEndTs,
            Boolean isEnabled,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));
        isTimeStampValid(createdAtStartTs, createdAtEndTs);

        String searchText = Objects.toString(pageLink.getSearchText(), "");

        if (searchText != null) {
            searchText = escapeSearchText(searchText);
        }

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<AppUser> userPage = null;
        if(currentUser.getAuthority().equals(AuthorityType.SYS_ADMIN.name())) {
            userPage = userRepository.findUsersBySysAdmin(
                    searchText,
                    isSearchMatchCase,
                    AuthorityType.CUSTOMER_USER,
                    convertTimestampToDateTime(createdAtStartTs),
                    convertTimestampToDateTime(createdAtEndTs),
                    isEnabled,
                    pageable
            );
        }
        Page<UserProfileResponse> userDtoPage = userPage.map(user ->
                getUserProfile(user.getId())
        );
        return new PageData<>(userDtoPage);
    }

    private String escapeSearchText(String searchText) {
        return searchText.replace("%", "\\%")
                .replace("_", "\\_")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^");
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null && endTs != null) {
            if (!(startTs >= 0 && endTs >= 0 && startTs <= endTs)) {
                throw new BadRequestException("Start time and end time must be valid");
            }
        }
    }


    @Override
    public String getUserAvatarById(UUID id) {
        AppUser user = userRepository.findById(id).orElse(null);
        assert user != null;
        return user.getAvatar();
    }

    @Override
    public String handleActiveUser(UUID id, Boolean isActive, AppUserDto currentUser) {
        UserCredential userCredential = userCredentialsRepository.findByUserId(id).get();
        ObjectMapper objectMapper = new ObjectMapper();
        if (isActive && userCredential.isEnabled()) {
            logService.save(LogDto.builder()
                    .entityId(id)
                    .actionData(objectMapper.valueToTree(new UserActivateRequest(id, true)))
                    .actionType(ActionType.ATTRIBUTES_UPDATED)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionFailureDetails(String.format("User with id [%s] has already activated", id))
                    .build(), currentUser);
            throw new BadRequestException(
                    String.format("User with id [%s] has already activated", id)
            );
        }
        if (!isActive && !userCredential.isEnabled()) {
            logService.save(LogDto.builder()
                    .entityId(id)
                    .actionData(objectMapper.valueToTree(new UserActivateRequest(id, false)))
                    .actionType(ActionType.ATTRIBUTES_UPDATED)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionFailureDetails(String.format("User with id [%s] has already deactivated", id))
                    .build(), currentUser);
            throw new BadRequestException(
                    String.format("User with id [%s] has already deactivated", id)
            );
        }
        userCredential.setEnabled(isActive);
        String state = Boolean.TRUE.equals(isActive) ? "activate" : "deactivate";
        logService.save(LogDto.builder()
                .entityId(id)
                .actionData(objectMapper.valueToTree(new UserActivateRequest(id, isActive)))
                .actionType(ActionType.ATTRIBUTES_UPDATED)
                .actionStatus(ActionStatus.SUCCESS)
                .build(), currentUser);
        return String.format("User with id [%s] has [%s] successful", id, state);
    }

    @Override
    public AppUserDto findByEmail(String email) {
        AppUser user = userRepository.findByEmail(email);
        return userMapper.toDto(user);
    }

    @Override
    public String syncUsers() {
        userRepository.findAll().forEach(appUser -> {
            userProducer.sendMessage(userMapper.toDto(appUser));
        });
        return "Sync users successfully";
    }

    private void checkIfEmailExist(String email, AppUserDto currentUser, Object actionData) {
        AppUser user = userRepository.findByEmail(email);
        if (user != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .actionData(objectMapper.valueToTree(actionData))
                    .actionType(ActionType.CREATED)
                    .actionFailureDetails(String.format("User with email [%s] is already exist", email))
                    .actionStatus(ActionStatus.FAILURE)
                    .build(), currentUser);
            throw new BadRequestException(
                    String.format("User with email [%s] is already exist", email)
            );
        }
    }

    private AppUser checkUserId(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id [%s] is not found", id))
        );
    }

    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
        return timestamp != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
