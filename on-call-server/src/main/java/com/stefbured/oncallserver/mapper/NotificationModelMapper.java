package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.NotificationDTO;
import com.stefbured.oncallserver.model.entity.notification.Notification;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(NotificationModelMapper.NOTIFICATION_MODEL_MAPPER)
public class NotificationModelMapper extends OnCallModelMapper {
    public static final String NOTIFICATION_MODEL_MAPPER = "notificationModelMapper";

    public static final String NOTIFICATION_TO_FULL_DTO = "notificationToFullDto";

    @Autowired
    @Qualifier(NOTIFICATION_TO_FULL_DTO)
    private void setUserToPostRegistrationDtoConverter(Converter<Notification, NotificationDTO> converter) {
        createTypeMap(Notification.class, NotificationDTO.class, NOTIFICATION_TO_FULL_DTO).setConverter(converter);
    }
}
