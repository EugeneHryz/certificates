package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import static com.epam.esm.repository.dao.query.DatabaseName.*;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(Tag entity) throws DaoException {
        entityManager.persist(entity);

        return entity.getId();
    }

    @Override
    public Optional<Tag> findById(int id) throws DaoException {
        Tag tag = entityManager.find(Tag.class, id);

        return Optional.ofNullable(tag);
    }

    @Override
    public List<Tag> getTags(int limit, int offset) throws DaoException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery.select(tagRoot));
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Tag> criteriaDelete = criteriaBuilder.createCriteriaDelete(Tag.class);

        Root<Tag> rootTag = criteriaDelete.from(Tag.class);
        criteriaDelete.where(criteriaBuilder.equal(rootTag.get(TAG_ID), id));

        Query query = entityManager.createQuery(criteriaDelete);
        return query.executeUpdate() > 0;
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
        criteriaQuery.select(rootTag).where(criteriaBuilder.equal(rootTag.get(TAG_NAME), name));
        TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);

        return query.getResultStream().findFirst();
    }

    @Override
    public List<Tag> findTagsForCertificate(GiftCertificate cert) throws DaoException {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, cert.getId());
        return certificate.getTags();
    }

    @Override
    public long getCount() throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Tag.class)));
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
}
