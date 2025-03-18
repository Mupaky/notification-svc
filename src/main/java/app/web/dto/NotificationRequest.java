package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class NotificationRequest {

    @NonNull
    private UUID userId;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}
