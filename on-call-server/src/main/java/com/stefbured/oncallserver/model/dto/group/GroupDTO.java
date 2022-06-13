package com.stefbured.oncallserver.model.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Group.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO {
    private Long id;

    @Size(max = MAX_GROUP_ID_TAG_LENGTH, message = GROUP_ID_TAG_LENGTH_ERROR_MESSAGE)
    private String idTag;

    @Size(min = MIN_GROUP_NAME_LENGTH, max = MAX_GROUP_NAME_LENGTH, message = GROUP_NAME_LENGTH_ERROR_MESSAGE)
    private String name;

    @Size(max = MAX_GROUP_DESCRIPTION_LENGTH, message = GROUP_DESCRIPTION_LENGTH_ERROR_MESSAGE)
    private String description;

    @PastOrPresent(message = CREATION_DATE_TIME_VALIDATION_ERROR_MESSAGE)
    private LocalDateTime creationDateTime;

    private String avatarUrl;

    private String avatarThumbnailUrl;

    private String mediumAvatarUrl;

    private String deleteAvatarUrl;

    private UserDTO creator;

    private GroupDTO parentGroup;

    private Boolean isMember = null;

    private Set<UserGrantDTO> userGrants;

    private Set<GroupDTO> childGroups;

    private Set<ScheduleRecordDTO> scheduleRecords;

    private Set<ChatDTO> chats;
}
