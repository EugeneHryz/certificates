package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.Tag_;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@PropertySource("classpath:query.properties")
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${dao.findTagOfUserWithHighestSpending}")
    private String findTagOfUserWithHighestSpending;

    @Override
    public int create(Tag entity) throws DaoException {
        try {
            entityManager.persist(entity);

            return entity.getId();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to create new Tag", e);
        }
    }

    @Override
    public Optional<Tag> findById(int id) throws DaoException {
        try {
            Tag tag = entityManager.find(Tag.class, id);

            return Optional.ofNullable(tag);
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find Tag by id = " + id, e);
        }
    }

    @Override
    public List<Tag> getTags(int limit, int offset) throws DaoException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery.select(tagRoot));
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        try {
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to get tags", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Tag> criteriaDelete = criteriaBuilder.createCriteriaDelete(Tag.class);

        Root<Tag> rootTag = criteriaDelete.from(Tag.class);
        criteriaDelete.where(criteriaBuilder.equal(rootTag.get(Tag_.id), id));
        Query query = entityManager.createQuery(criteriaDelete);

        try {
            return query.executeUpdate() > 0;
        } catch (PersistenceException e) {
            throw new DaoException("Unable to delete Tag by id");
        }
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Update operation is not supported for tags");
    }

    @Override
    public Optional<Tag> findByName(String name) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> rootTag = criteriaQuery.from(Tag.class);
        criteriaQuery.select(rootTag).where(criteriaBuilder.equal(rootTag.get(Tag_.name), name));
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);

        try {
            return query.getResultStream().findFirst();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find Tag by name", e);
        }
    }

    @Override
    public long getCount() throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Tag.class)));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        try {
            return query.getSingleResult();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to count all tags", e);
        }
    }

    @Override
    public Optional<Tag> findMostWidelyUsedTagOfUserWithHighestSpending() throws DaoException {
        Query query = entityManager.createNativeQuery(findTagOfUserWithHighestSpending, Tag.class);

        try {
            return Optional.of((Tag) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find most widely used Tag of a user with highest spending", e);
        }
    }
}
