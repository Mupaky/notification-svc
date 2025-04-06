package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
