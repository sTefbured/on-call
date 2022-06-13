package com.stefbured.oncallserver.mapper.converter.notification;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.NotificationDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.notification.Notification;
import com.stefbured.oncallserver.model.entity.notification.NotificationType;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.NotificationModelMapper.NOTIFICATION_TO_FULL_DTO;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@Component(NOTIFICATION_TO_FULL_DTO)
public class NotificationToFullDtoConverter implements Converter<Notification, NotificationDTO> {
    private Converter<User, UserDTO> userToPreviewDtoConverter;

    @Override
    public NotificationDTO convert(MappingContext<Notification, NotificationDTO> context) {
        var source = context.getSource();
        var destination = new NotificationDTO();
        destination.setId(source.getId());
        destination.setCreationDate(source.getCreationDate());
        destination.setStatusChangeDate(source.getStatusChangeDate());
        destination.setIsActive(source.isActive());
        var creatorContext = new OnCallMappingContext<User, UserDTO>(source.getCreator());
        destination.setCreator(userToPreviewDtoConverter.convert(creatorContext));
        destination.setNotificationText(source.getNotificationText());
        destination.setSourceObjectId(source.getSourceObjectId());
        var notificationType = new NotificationType();
        notificationType.setId(source.getNotificationType().getId());
        notificationType.setName(source.getNotificationType().getName());
        destination.setNotificationType(notificationType);
        var targetUserContext = new OnCallMappingContext<User, UserDTO>(source.getTargetUser());
        destination.setTargetUser(userToPreviewDtoConverter.convert(targetUserContext));
        return destination;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        this.userToPreviewDtoConverter = converter;
    }
}
