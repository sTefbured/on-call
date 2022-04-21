package com.stefbured.oncallserver.utils;

import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.stream.Collectors;

public class OnCallModelMapper extends ModelMapper {
    // Group mappings
    public static final String GROUP_TO_MEMBER_VIEW_DTO = "groupToMemberViewDto";
    public static final String GROUP_TO_ADMIN_VIEW_DTO = "groupToAdminViewDto";
    public static final String GROUP_TO_PREVIEW_DTO = "groupToPreviewDto";

    // User mappings
    public static final String USER_TO_PRIVATE_INFORMATION_DTO = "userToPrivateInformationDto";
    public static final String USER_TO_PUBLIC_INFORMATION_DTO = "userToPublicInformationDto";
    public static final String USER_TO_PREVIEW_DTO = "userToPreviewDto";
    public static final String USER_TO_POST_REGISTRATION_DTO = "userToPostRegistrationDto";

    // UserGrant mappings
    public static final String USER_GRANT_TO_SHORT_DTO_FOR_USER = "userGrantToShortDtoForUser";

    public OnCallModelMapper() {
        addGroupMappings();
        addUserToDtoMappings();
        addUserGrantMappings();
    }

    public void mapSkippingNullValues(Object source, Object destination) {
        var isSkipNullEnabledOld = getConfiguration().isSkipNullEnabled();
        var oldMatchingStrategy = getConfiguration().getMatchingStrategy();
        getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);
        map(source, destination);
        getConfiguration().setSkipNullEnabled(isSkipNullEnabledOld).setMatchingStrategy(oldMatchingStrategy);
    }

    private void addUserToDtoMappings() {
        createTypeMap(User.class, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    destination.setBirthDate(source.getBirthDate());
                    destination.setRegistrationDateTime(source.getRegistrationDateTime());
                    destination.setLastVisitDateTime(source.getLastVisitDateTime());
                    destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
                    destination.setIsBanned(source.isBanned());
                    destination.setIsEnabled(source.isEnabled());
                    if (source.getGrants() != null) {
                        var grantMapper = getTypeMap(UserGrant.class, UserGrantDTO.class, USER_GRANT_TO_SHORT_DTO_FOR_USER);
                        destination.setGrants(source.getGrants().stream()
                                .map(grantMapper::map)
                                .toList());
                    }
                    if (source.getCreatedGroups() != null) {
                        var groupMapper = getTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
                        destination.setCreatedGroups(source.getCreatedGroups().stream()
                                .map(groupMapper::map)
                                .collect(Collectors.toSet()));
                    }
                    //TODO: add schedule records and created chats
                    return destination;
                });
        createTypeMap(User.class, UserDTO.class, USER_TO_PUBLIC_INFORMATION_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    destination.setBirthDate(source.getBirthDate());
                    destination.setRegistrationDateTime(source.getRegistrationDateTime());
                    destination.setLastVisitDateTime(source.getLastVisitDateTime());
                    destination.setIsBanned(source.isBanned());
                    return destination;
                });
        createTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    return destination;
                });

        createTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    destination.setBirthDate(source.getBirthDate());
                    destination.setRegistrationDateTime(source.getRegistrationDateTime());
                    destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
                    return destination;
                });

    }

    private void addUserGrantMappings() {
        createTypeMap(UserGrant.class, UserGrantDTO.class, USER_GRANT_TO_SHORT_DTO_FOR_USER)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserGrantDTO();
                    destination.setId(source.getId());
                    if (source.getGroup() != null) {
                        var typeMapper = getTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
                        destination.setGroup(typeMapper.map(source.getGroup()));
                    }
                    if (source.getChat() != null) {
//                        TODO: add chat mapping after its implementation
//                        var typeMapper = getTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO);
//                        destination.setUser(typeMapper.map(source.getUser()));
                    }

//                    TODO: add role mapping after its implementation
//                    var roleMapper = getTypeMap(Role.class, RoleDTO.class, USER_TO_PREVIEW_DTO);
//                    destination.setRole(roleMapper.map(source.getRole()));
                    return destination;
                });
    }

    private void addGroupMappings() {
        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_MEMBER_VIEW_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var limitedGroupMapper = getTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
                    var parentGroup = source.getParentGroup() == null
                            ? null
                            : limitedGroupMapper.map(source.getParentGroup());
                    var childGroups = source.getChildGroups() == null ? null : source.getChildGroups().stream()
                            .map(limitedGroupMapper::map)
                            .collect(Collectors.toSet());
                    var limitedUserMapper = getTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO);
                    var creator = limitedUserMapper.map(source.getCreator());
                    var destination = new GroupDTO();
                    destination.setId(source.getId());
                    destination.setName(source.getName());
                    destination.setDescription(source.getDescription());
                    destination.setCreationDateTime(source.getCreationDateTime());
                    destination.setCreator(creator);
                    destination.setParentGroup(parentGroup);
                    destination.setChildGroups(childGroups);
                    return destination;
                });

        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var limitedGroupMapper = getTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
                    var parentGroup = source.getParentGroup() == null
                            ? null
                            : limitedGroupMapper.map(source.getParentGroup());
                    var childGroups = source.getChildGroups() == null ? null : source.getChildGroups().stream()
                            .map(limitedGroupMapper::map)
                            .collect(Collectors.toSet());
                    var limitedUserMapper = getTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO);
                    var creator = limitedUserMapper.map(source.getCreator());
                    var destination = new GroupDTO();
                    destination.setId(source.getId());
                    destination.setIdTag(source.getIdTag());
                    destination.setName(source.getName());
                    destination.setDescription(source.getDescription());
                    destination.setCreationDateTime(source.getCreationDateTime());
                    destination.setCreator(creator);
                    destination.setParentGroup(parentGroup);
                    destination.setChildGroups(childGroups);
                    return destination;
                });

        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new GroupDTO();
                    destination.setId(source.getId());
                    destination.setIdTag(source.getIdTag());
                    destination.setName(source.getName());
                    destination.setDescription(source.getDescription());
                    return destination;
                });
    }
}
