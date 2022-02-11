package com.epam.esm.repository.entity;

import com.epam.esm.repository.entity.listener.EntityAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditing_history")
public class AuditHistory extends AbstractEntity {

    @Column(name = "entity_content", length = 1024)
    private String entityContent;

    @CreatedDate
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private EntityAction action;

    public AuditHistory() {
    }

    public AuditHistory(String entityContent, EntityAction action) {
        this.entityContent = entityContent;
        this.lastUpdated = LocalDateTime.now();
        this.action = action;
    }

    public String getEntityContent() {
        return entityContent;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public EntityAction getAction() {
        return action;
    }
}
