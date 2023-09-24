package com.example.Cloud.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class UserFcmTokenDetails {
    public UserFcmTokenDetails() {
    }
    @Id
    private String kid;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fcmToken;
    private String deviceType;
    private String deviceUuid;

    private String project;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime updatedTime;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public OffsetDateTime getUpdatedTime() {
        return updatedTime;
    }
    public UserFcmTokenDetails(String kid, int id, String fcmToken, String deviceType, String deviceUuid, String project, OffsetDateTime updatedTime) {
        this.kid = kid;
        this.id = id;
        this.fcmToken = fcmToken;
        this.deviceType = deviceType;
        this.deviceUuid = deviceUuid;
        this.project = project;
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "UserFcmTokenDetails{" +
                "kid='" + kid + '\'' +
                ", id=" + id +
                ", fcmToken='" + fcmToken + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", project='" + project + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }

    public void setUpdatedTime(OffsetDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

}
