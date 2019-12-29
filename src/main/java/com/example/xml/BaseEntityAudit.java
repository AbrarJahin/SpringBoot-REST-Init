package com.example.xml;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * Base Entity Audit
 *
 * @author cem ikta
 */
@MappedSuperclass
public abstract class BaseEntityAudit extends BaseEntity {

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Zagreb")
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    /*
    @Size(max = 20)
    @Column(name = "created_by", length = 20)
    private String createdBy;

    @Size(max = 20)
    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    @Size(max = 20)
    @Column(name = "created_ip", length = 50)
    private String createdIP;

    @Size(max = 20)
    @Column(name = "updated_ip", length = 50)
    private String updatedIP;
    */

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Sets createdAt before insert
     */
    @PrePersist
    public void setCreationDate() {
        this.createdAt = new Date();
    }

    /**
     * Sets updatedAt before update
     */
    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = new Date();
    }
}