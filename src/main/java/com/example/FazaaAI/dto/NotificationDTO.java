package com.example.FazaaAI.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
    private boolean isRead;
    private Long matchRequestId; // ✅ Expose this field

    public NotificationDTO(Long id, String message, String type, boolean isRead, Long matchRequestId) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.matchRequestId = matchRequestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getMatchRequestId() {
        return matchRequestId;
    }

    public void setMatchRequestId(Long matchRequestId) {
        this.matchRequestId = matchRequestId;
    }
}