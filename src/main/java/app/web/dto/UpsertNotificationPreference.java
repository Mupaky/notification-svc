package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class UpsertNotificationPreference {

    @NonNull
    private UUID userId;

    private boolean notificationEnabled;

    @NonNull
    private NotificationTypeRequest type;

    @NonNull
    @NotBlank
    private String contactInfo;
}
