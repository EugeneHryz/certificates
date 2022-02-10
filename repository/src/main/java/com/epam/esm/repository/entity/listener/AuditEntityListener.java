package com.epam.esm.repository.entity.listener;

import com.epam.esm.repository.entity.AbstractEntity;
import com.epam.esm.repository.entity.AuditHistory;

import javax.persistence.*;

public class AuditEntityListener {

    @PrePersist
    public void prePersist(AbstractEntity entity) {
        saveAction(entity, EntityAction.INSERT);
    }

    @PreUpdate
    public void preUpdate(AbstractEntity entity) {
        saveAction(entity, EntityAction.UPDATE);
    }

    @PreRemove
    public void preRemove(AbstractEntity entity) {
        saveAction(entity, EntityAction.DELETE);
    }

    private void saveAction(AbstractEntity entity, EntityAction action) {
        EntityManagerFactory emFactory = BeanUtil.getBean(EntityManagerFactory.class);
        EntityManager entityManager = emFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.persist(new AuditHistory(entity.toString(), action));

        tx.commit();
    }
}
