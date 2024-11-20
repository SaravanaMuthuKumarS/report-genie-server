package com.i2i.rgs.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public void setAudit(String userId) {
        this.createdAt = new Date();
        this.createdBy = userId;
        this.isDeleted = false;
    }
}
