package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.entity.GiftCertificate_;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.Tag_;
import com.epam.esm.repository.searchoption.CertificateSearchParameter;
import com.epam.esm.repository.entity.GiftCertificate;

import com.epam.esm.repository.dao.GiftCertificateDao;
import com.epam.esm.repository.exception.DaoException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Subquery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int create(GiftCertificate certificate) throws DaoException {
        entityManager.persist(certificate);

        try {
            return certificate.getId();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to create GiftCertificate", e);
        }
    }

    @Override
    public Optional<GiftCertificate> findById(int id) throws DaoException {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);

        try {
            return Optional.ofNullable(certificate);
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find GiftCertificate by id = " + id, e);
        }
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> rootCert = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(rootCert).where(criteriaBuilder.equal(rootCert.get(GiftCertificate_.name), name));
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);

        try {
            return query.getResultStream().findFirst();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find GiftCertificate by name", e);
        }
    }

    @Override
    public List<GiftCertificate> findCertificates(CertificateSearchParameter options, int limit, int offset) throws DaoException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> mainQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Subquery<Integer> subquery = mainQuery.subquery(Integer.class);

        Root<GiftCertificate> rootCert = subquery.from(GiftCertificate.class);
        if (options.getTagNames() != null && options.getTagNames().length > 0) {
            ListJoin<GiftCertificate, Tag> certTags = rootCert.join(GiftCertificate_.tags);

            subquery.where(certTags.get(Tag_.name).in((Object[]) options.getTagNames()));
            subquery.groupBy(rootCert.get(GiftCertificate_.id));
            subquery.having(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.count(certTags
                            .get(GiftCertificate_.id)), options.getTagNames().length)));
        }
        subquery.select(rootCert.get(GiftCertificate_.id));

        Root<GiftCertificate> mainRoot = mainQuery.from(GiftCertificate.class);
        String searchPattern = "%" + options.getSearchParam() + "%";
        Predicate searchPredicate = criteriaBuilder.or(criteriaBuilder.like(mainRoot.get(GiftCertificate_.name), searchPattern),
                criteriaBuilder.like(mainRoot.get(GiftCertificate_.description), searchPattern));
        mainQuery.select(mainRoot);

        if (options.getTagNames() != null && options.getTagNames().length > 0) {
            mainQuery.where(criteriaBuilder.and(mainRoot.get(GiftCertificate_.id).in(subquery), searchPredicate));
        } else {
            mainQuery.where(searchPredicate);
        }
        TypedQuery<GiftCertificate> query = entityManager.createQuery(mainQuery);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        try {
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to find GiftCertificate by parameters", e);
        }
    }

    @Override
    public boolean deleteById(int id) throws DaoException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<GiftCertificate> criteriaDelete = criteriaBuilder.createCriteriaDelete(GiftCertificate.class);

        Root<GiftCertificate> rootCert = criteriaDelete.from(GiftCertificate.class);
        criteriaDelete.where(criteriaBuilder.equal(rootCert.get(GiftCertificate_.id), id));

        Query query = entityManager.createQuery(criteriaDelete);
        try {
            return query.executeUpdate() > 0;
        } catch (PersistenceException e) {
            throw new DaoException("Unable to delete GiftCertificate by id", e);
        }
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate cert) throws DaoException {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, cert.getId());
        if (certificate != null) {
            certificate.setName(cert.getName());
            certificate.setDescription(cert.getDescription());
            certificate.setDuration(cert.getDuration());
            certificate.setPrice(cert.getPrice());
            certificate.setCreated(cert.getCreated());
            certificate.setLastUpdated(cert.getLastUpdated());
        }
        try {
            return Optional.ofNullable(certificate);
        } catch (PersistenceException e) {
            throw new DaoException("Unable to update GiftCertificate with id = " + cert.getId(), e);
        }
    }

    @Override
    public long getCount(CertificateSearchParameter options) throws DaoException {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> mainQuery = criteriaBuilder.createQuery(Long.class);
        Subquery<Integer> subquery = mainQuery.subquery(Integer.class);

        Root<GiftCertificate> rootCert = subquery.from(GiftCertificate.class);
        if (options.getTagNames() != null && options.getTagNames().length > 0) {
            ListJoin<GiftCertificate, Tag> certTags = rootCert.join(GiftCertificate_.tags);

            subquery.where(certTags.get(Tag_.name).in((Object[]) options.getTagNames()));
            subquery.groupBy(rootCert.get(GiftCertificate_.id));
            subquery.having(criteriaBuilder.and(criteriaBuilder.equal(criteriaBuilder.count(certTags
                    .get(GiftCertificate_.id)), options.getTagNames().length)));
        }
        subquery.select(rootCert.get(GiftCertificate_.id));

        Root<GiftCertificate> mainRoot = mainQuery.from(GiftCertificate.class);
        String searchPattern = "%" + options.getSearchParam() + "%";
        Predicate searchPredicate = criteriaBuilder.or(criteriaBuilder.like(mainRoot.get(GiftCertificate_.name), searchPattern),
                criteriaBuilder.like(mainRoot.get(GiftCertificate_.description), searchPattern));
        mainQuery.select(criteriaBuilder.count(mainRoot));

        if (options.getTagNames() != null && options.getTagNames().length > 0) {
            mainQuery.where(criteriaBuilder.and(mainRoot.get(GiftCertificate_.id).in(subquery), searchPredicate));
        } else {
            mainQuery.where(searchPredicate);
        }

        try {
            return entityManager.createQuery(mainQuery).getSingleResult();
        } catch (PersistenceException e) {
            throw new DaoException("Unable to count certificates filtered by parameters", e);
        }
    }
}
