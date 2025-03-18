package app.service;

import app.model.Notification;
import app.model.NotificationPreference;
import app.model.NotificationStatus;
import app.model.NotificationType;
import app.repository.NotificationPreferenceRepository;
import app.repository.NotificationRepository;
import app.web.dto.NotificationRequest;
import app.web.dto.UpsertNotificationPreference;
import app.web.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {


    private final NotificationPreferenceRepository preferenceRepository;
    private final MailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationPreferenceRepository preferenceRepository, MailSender mailSender, NotificationRepository notificationRepository) {
        this.preferenceRepository = preferenceRepository;
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public NotificationPreference upsertPreference(UpsertNotificationPreference dto){

        Optional<NotificationPreference> userNotificationPreferenceOptional = preferenceRepository.findByUserId(dto.getUserId());

        if(userNotificationPreferenceOptional.isPresent()){
            NotificationPreference preference = userNotificationPreferenceOptional.get();
            preference.setContactInfo(dto.getContactInfo());
            preference.setUserId(dto.getUserId());
            preference.setType(DtoMapper.fromNotificationTypeRequest(dto.getType()));
            preference.setEnabled(dto.isNotificationEnabled());
            return preferenceRepository.save(preference);
        }

        NotificationPreference notificationPreference = NotificationPreference.builder()
                .userId(dto.getUserId())
                .type(DtoMapper.fromNotificationTypeRequest(dto.getType()))
                .isEnabled(dto.isNotificationEnabled())
                .contactInfo(dto.getContactInfo())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        return preferenceRepository.save(notificationPreference);
    }


    public NotificationPreference getPreferenceBNyUserId(UUID userId) {

        return preferenceRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("Notification preference for user %s not found.".formatted(userId)));
    }

    public Notification sendNotification(NotificationRequest notificationRequest){

        UUID userId = notificationRequest.getUserId();
        NotificationPreference userPreference = getPreferenceBNyUserId(userId);

        if(!userPreference.isEnabled()){
            throw  new IllegalArgumentException("User with id %s does not allow to receive notifications.".formatted(userId));
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userPreference.getContactInfo());
        message.setSubject(notificationRequest.getSubject());
        message.setText(notificationRequest.getBody());

        Notification notification = Notification.builder()
                .subject(notificationRequest.getSubject())
                .body(notificationRequest.getBody())
                .createdOn(LocalDateTime.now())
                .status(NotificationStatus.SUCCEEDED)
                .userId(userId)
                .isDeleted(false)
                .type(NotificationType.EMAIL)
                .build();

        try {
            mailSender.send(message);
            notification.setStatus(NotificationStatus.SUCCEEDED);
        }catch (Exception e){
            notification.setStatus(NotificationStatus.FAILED);
            log.warn("There was issue sending an email to %s due to %s".formatted(userPreference.getContactInfo(), e.getMessage()));
        }


        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationHistory(UUID userId) {

        return notificationRepository.findAllByUserIdAndDeletedIsFalse(userId);
    }
}

