package com.stefbured.oncallserver.config;

import com.stefbured.oncallserver.model.dto.RoleDTO;
import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.UserGroup;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfiguration {
    // UserGroup mappings
    public static final String USER_GROUP_TO_DTO = "userGroupToDto";
    public static final String USER_GROUP_TO_LIMITED_DTO = "userGroupToLimitedDto";
    public static final String DTO_TO_USER_GROUP = "dtoToUserGroup";

    // User mappings
    public static final String USER_TO_DTO = "userToDto";
    public static final String USER_TO_LIMITED_DTO = "userToLimitedDto";
    public static final String USER_TO_POST_REGISTRATION_DTO = "userToPostRegistrationDto";
    public static final String REGISTRATION_DTO_TO_USER = "registrationDtoToUser";

    // Role mappings
    public static final String LIMITED_DTO_TO_ROLE = "limitedDtoToRole";

    private PasswordEncoder passwordEncoder;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        addUserGroupMappings(modelMapper);
        addDtoToUserMappings(modelMapper);
        addUserToDtoMappings(modelMapper);
        addDtoToRoleMappings(modelMapper);
        return modelMapper;
    }

    private void addUserToDtoMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(User.class, UserDTO.class, USER_TO_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    destination.setBirthDate(source.getBirthDate());
                    destination.setRegistrationDate(source.getRegistrationDate());
                    destination.setLastVisitDate(source.getLastVisitDate());
                    destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
                    destination.setUserExpirationDate(source.getUserExpirationDate());
                    destination.setIsBanned(source.isBanned());
                    destination.setIsEnabled(source.isEnabled());
                    // TODO: roles and user groups??
                    return destination;
                });
        modelMapper.createTypeMap(User.class, UserDTO.class, USER_TO_LIMITED_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    fillLimitedUserDtoParameters(source, destination);
                    return destination;
                });

        modelMapper.createTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserDTO();
                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    destination.setFirstName(source.getFirstName());
                    destination.setLastName(source.getLastName());
                    destination.setBirthDate(source.getBirthDate());
                    destination.setRegistrationDate(source.getRegistrationDate());
                    destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
                    destination.setUserExpirationDate(source.getUserExpirationDate());
                    return destination;
                });

    }

    private void addDtoToUserMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserDTO.class, User.class, REGISTRATION_DTO_TO_USER)
                .setConverter(context -> {
                    var source = context.getSource();
                    var roleMapper = modelMapper.getTypeMap(RoleDTO.class, Role.class, LIMITED_DTO_TO_ROLE);
                    var roles = source.getRoles().stream()
                            .map(roleMapper::map)
                            .collect(Collectors.toSet());
                    return User.builder()
                            .username(source.getUsername())
                            .password(passwordEncoder.encode(source.getPassword()))
                            .birthDate(source.getBirthDate())
                            .email(source.getEmail())
                            .registrationDate(LocalDateTime.now())
                            .firstName(source.getFirstName())
                            .lastName(source.getLastName())
                            .isBanned(false)
                            .isEnabled(true)
                            .lastVisitDate(LocalDateTime.now())
                            .roles(roles)
                            .build();
                });
    }

    private void fillLimitedUserDtoParameters(User source, UserDTO destination) {
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }

    private void addUserGroupMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserGroup.class, UserGroupDTO.class, USER_GROUP_TO_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var limitedUserGroupMapper = modelMapper.getTypeMap(UserGroup.class, UserGroupDTO.class, USER_GROUP_TO_LIMITED_DTO);
                    var parentUserGroupDto = limitedUserGroupMapper.map(source.getParentGroup());
                    var limitedUserMapper = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_LIMITED_DTO);
                    var creator = limitedUserMapper.map(source.getCreator());
                    var owner = limitedUserMapper.map(source.getOwner());
                    var destination = new UserGroupDTO();
                    fillLimitedUserGroupDtoParameters(source, destination);
                    destination.setCreator(creator);
                    destination.setOwner(owner);
                    destination.setParentGroup(parentUserGroupDto);
                    return destination;
                });

        modelMapper.createTypeMap(UserGroup.class, UserGroupDTO.class, USER_GROUP_TO_LIMITED_DTO)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new UserGroupDTO();
                    fillLimitedUserGroupDtoParameters(source, destination);
                    return destination;
                });
    }

    private void fillLimitedUserGroupDtoParameters(UserGroup source, UserGroupDTO destination) {
        destination.setId(source.getId());
        destination.setIdTag(source.getIdTag());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setCreationDate(source.getCreationDate());
    }

    private void addDtoToRoleMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(RoleDTO.class, Role.class, LIMITED_DTO_TO_ROLE)
                .setConverter(context -> {
                    var source = context.getSource();
                    var destination = new Role();
                    destination.setId(source.getId());
                    return destination;
                });
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
