package com.woowacourse.zzinbros.notification.dto;

import com.woowacourse.zzinbros.notification.domain.PostNotification;

import java.time.OffsetDateTime;

public class NotificationResponseDto {
    private Long notifiedUserId;
    private Long postId;
    private String publisherName;
    private String publisherProfile;
    private String notificationType;
    private OffsetDateTime createdDateTime;

    public NotificationResponseDto() {
    }

    public NotificationResponseDto(PostNotification postNotification) {
        notifiedUserId = postNotification.getNotifiedUser().getId();
        postId = postNotification.getPost().getId();
        publisherName = postNotification.getPublisher().getName();
        publisherProfile = postNotification.getPublisher().getProfile().getUrl();
        notificationType = postNotification.getType().toString();
        createdDateTime = postNotification.getCreatedDateTime();
    }

    public Long getNotifiedUserId() {
        return notifiedUserId;
    }

    public void setNotifiedUserId(Long notifiedUserId) {
        this.notifiedUserId = notifiedUserId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherProfile() {
        return publisherProfile;
    }

    public void setPublisherProfile(String publisherProfile) {
        this.publisherProfile = publisherProfile;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public OffsetDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(OffsetDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
