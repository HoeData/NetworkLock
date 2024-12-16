package com.ruoyi.license.utils;

import java.time.LocalDateTime;

public class StartLicenseVO {

    private String productId;
    private LocalDateTime endTime;
    private Boolean temporaryAuthorization = false;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getTemporaryAuthorization() {
        return temporaryAuthorization;
    }

    public void setTemporaryAuthorization(Boolean temporaryAuthorization) {
        this.temporaryAuthorization = temporaryAuthorization;
    }
}
